package com.muistipeli;

import java.io.File;
import java.sql.*;
import java.util.*;

public class Database {

    private Connection dbConnection = null;
    private static Database dbInstance = null;

    private Database() {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Tietokantaan ei saatu yhteyttä:" + e.getMessage());
        }

    }

    /********* Singleton- malli *********/
    public static synchronized Database getInstance() {
        if (dbInstance == null) {
            dbInstance = new Database();
        }
        return dbInstance;
    }

    /********* Tietokannan alustus *********/
    private boolean initializeDatabase() throws SQLException {
        if (dbConnection != null) {
            String pakkaSQL = "CREATE TABLE pakat (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE);";
            String korttiSQL = "CREATE TABLE kortit (sana VARCHAR(100) NOT NULL, kaannos VARCHAR(100) NOT NULL, pakka_id INTEGER, FOREIGN KEY (pakka_id) REFERENCES pakat(name) ON DELETE CASCADE);";

            try (Statement createStatement = dbConnection.createStatement()) {
                createStatement.executeUpdate(pakkaSQL);
                createStatement.executeUpdate(korttiSQL);
                System.out.println("Tietokanta luotu onnistuneesti");
                return true;
            } catch (SQLException e) {
                System.err.println("Virhe havaittu luodessa SQL pöytää: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Tietokannan yhteyttä ei ole muodostettu");
            return false;
        }
    }

    /********* Avaa tietokantayhteyden *********/
    public void open() throws SQLException {
        boolean exit;

        File file = new File("Database.db");
        exit = file.exists();

        String databaseUrl = "jdbc:sqlite:" + "Database.db";
        dbConnection = DriverManager.getConnection(databaseUrl);
        if (!exit) {
            System.out.println("Tietokantaa ei löytynyt, alustetaan...");
            initializeDatabase();
        } else {
            System.out.println("Tietokantayhteys avattu.");
        }
    }

    /********* Sulje tietokantayhteys *********/
    public void close() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
                System.out.println("Tietokanta yhteys on kiinni");
            }
        } catch (SQLException e) {
            System.out.println("Virhe havaittu suljettaessa tietokanta yhteyttä: " + e.getMessage());
        }
    }

    /********* Luodaan uusi kortti *********/
    public void addKortti(String sana, String kaannos, String pakka) throws SQLException {
        int pakkaId = getPakka(pakka);
        if (pakkaId == -1) {
            System.err.println("Pakkaa nimeltä '" + pakka + "' ei ole olemassa. Korttia ei luotu.");
            return;
        }

        String korttiSQL = "INSERT INTO kortit (sana, kaannos, pakka_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(korttiSQL)) {
            stmt.setString(1, sana);
            stmt.setString(2, kaannos);
            stmt.setString(3, pakka);
            stmt.executeUpdate();
            System.out.println("Kortti luotu onnistuneesti.");
        } catch (SQLException e) {
            System.err.println("Virhe korttia luodessa: " + e.getMessage());
            throw e;
        }
    }

    /********* Haetaan kaikki kortit pakasta *********/
    public HashMap<String, String> getKortit(String pakkaName) throws SQLException {
        HashMap<String, String> cards = new HashMap<>();
        String sql = "SELECT k.sana, k.kaannos, k.pakka_id FROM kortit k INNER JOIN pakat p ON k.pakka_id = p.name WHERE p.name = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, pakkaName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String sana = rs.getString("sana");
                    String kaannos = rs.getString("kaannos");
                    cards.put(sana, kaannos);
                }
            }
        } catch (SQLException e) {
            System.err.println("Virhe haettaessa kortteja pakasta '" + pakkaName + "': " + e.getMessage());
        }
        return cards;
    }

    public void setKortti(String originalSana, String newSana, String newKaannos) throws SQLException {
        String updateSQL = "UPDATE kortit SET sana = ?, kaannos = ? WHERE sana = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(updateSQL)) {
            pstmt.setString(1, newSana);
            pstmt.setString(2, newKaannos);
            pstmt.setString(3, originalSana);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Kortti päivitetty onnistuneesti.");
            } else {
                throw new SQLException("Virhe");
            }
        } catch (SQLException e) {
            System.err.println("Virhe päivitettäessä korttia: " + e.getMessage());
            throw e;
        }
    }

    /********* Poista kortti kokonaisuus *********/
    public boolean deleteKortti(String sana, String kaannos) throws SQLException {
        String deleteSQL = "DELETE FROM kortit  WHERE sana = ? AND kaannos = ?";
        dbConnection.setAutoCommit(false);
        try (PreparedStatement pstmt = dbConnection.prepareStatement(deleteSQL)) {
            pstmt.setString(1, sana);
            pstmt.setString(2, kaannos);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Kortti poistettiin onnistuneesti");
                dbConnection.commit();
                return true;
            } else {
                throw new SQLException("Virhe");
            }
        } catch (SQLException ex) {
            dbConnection.rollback();
            System.err.println("Ilmeni virhe kun korttia yritettiin poistaa: " + ex.getMessage());
            return false;
        }
    }

    /********* Hae kaikki pakat *********/
    public HashMap<String, String> getAllPakka() throws SQLException {
        HashMap<String, String> pakat = new HashMap<>();
        String sql = "SELECT id, name FROM pakat";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                pakat.put(id, name);
            }
        } catch (SQLException e) {
            System.err.println("Virhe haettaessa pakkoja: " + e.getMessage());
            throw e;
        }
        return pakat;
    }

    /********* Tarkistaa onko pakka lista tyhjä *********/
    public boolean setPakkaName(String oldName, String newName) throws SQLException {
        // Tarkistetaan onko yhteys ja pakka olemassa
        if (dbConnection == null) {
            open();
        }
        if (getPakka(oldName) == -1) {
            System.err.println("Pakkaa nimeltä '" + oldName + "' ei ole olemassa.");
            return false;
        }

        // Aloitetaan pakan päivitys
        String updatePakkaSQL = "UPDATE pakat SET name = ? WHERE name = ?";
        String updateKorttiSQL = "UPDATE kortit set pakka_id = ? WHERE pakka_id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(updatePakkaSQL)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, oldName);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (PreparedStatement updateKorttiStmt = dbConnection.prepareStatement(updateKorttiSQL)) {
                    updateKorttiStmt.setString(1, newName);
                    updateKorttiStmt.setString(2, oldName);
                    updateKorttiStmt.executeUpdate();

                    System.out.println("Pakka '" + oldName + "' nimetty uudelleen muotoon '" + newName + "'.");
                    return true;
                } catch (SQLException ex) {
                    System.err.println("Virhe päivitettäessä pakan nimeä: " + ex.getMessage());
                    return false;
                }

            } else {
                System.err.println("Pakan nimeä ei päivitetty.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Virhe päivitettäessä pakan nimeä: " + e.getMessage());
            return false;
        }
    }

    /********* Hae pakka *********/
    public int getPakka(String pakkaName) throws SQLException {
        String sql = "SELECT id FROM pakat WHERE name = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, pakkaName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1; // Pakkaa ei löydy
            }
        } catch (SQLException e) {
            System.err.println("Virhe haettaessa pakkaa nimellä '" + pakkaName + "': " + e.getMessage());
            throw e;
        }
    }

    /********* Tarkistaa onko pakka lista tyhjä *********/
    public boolean hasPakka() throws SQLException {
        HashMap<String, String> pakat = getAllPakka();
        return !pakat.isEmpty();
    }

    /********* Poista koko pakka *********/
    public boolean deletePakka(String pakkaName) throws SQLException {
        String decksDeleteSQL = "DELETE FROM pakat WHERE name = ?";
        String cardsDeleteSQL = "DELETE FROM kortit WHERE pakka_id = ?";

        dbConnection.setAutoCommit(false);

        try (PreparedStatement deleteKortit = dbConnection.prepareStatement(cardsDeleteSQL)) {
            deleteKortit.setString(1, pakkaName);
            deleteKortit.executeUpdate();
        } catch (SQLException ex) {
            dbConnection.rollback();
            System.err.println("Ilmeni virhe kun kortteja yritettiin poistaa: " + ex.getMessage());
            return false;
        }

        try (PreparedStatement deletePakka = dbConnection.prepareStatement(decksDeleteSQL)) {
            deletePakka.setString(1, pakkaName);
            int decks = deletePakka.executeUpdate();
            if (decks > 0) {
                dbConnection.commit();
                return true;
            } else {
                throw new SQLException("Pakan poistossa ilmeni virhe");
            }
        } catch (SQLException ex) {
            dbConnection.rollback();
            System.err.println("Ilmeni virhe kun pakkaa yritettiin poistaa: " + ex.getMessage());
            return false;
        }
    }

    /********* Luodaan uusi pakka *********/
    public boolean addPakka(String pakkaNimi) {
        /*** Tarkistetaan onko yhteys tietokantaan ***/
        if (dbConnection == null) {
            System.err.println("Tietokantayhteys ei ole auki, yritetään avata yhteys.");
            try {
                open();
            } catch (SQLException e) {
                System.err.println("Tietokantayhteyden avaaminen epäonnistui: " + e.getMessage());
                return false;
            }
        }

        /*** Pakan lisäys ***/
        String sql = "INSERT INTO pakat (name) VALUES (?)";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, pakkaNimi);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Uusi pakka lisätty onnistuneesti: " + pakkaNimi);
                return true;
            } else {
                System.err.println("Pakkaa '" + pakkaNimi + "' ei lisätty. Ei muutettuja rivejä.");
                return false;
            }
        } catch (SQLException e) {
            /*** Virheenkäsittely jos yritetään puskea jo olemassa oleva pakka ***/
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Pakka nimeltä '" + pakkaNimi + "' on jo olemassa.");
            } else {
                System.err.println("Virhe lisättäessä pakkaa '" + pakkaNimi + "': " + e.getMessage());
            }
            return false;
        }
    }
}