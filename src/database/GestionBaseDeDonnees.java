/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import model.Livre;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Pierre-Nicolas
 */
public class GestionBaseDeDonnees {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:file:./database/bookDB";
    private static final String DB_USERNAME = "test";
    private static final String DB_PASSWORD = "test";
    private Connection connection;
    private Statement statement;
    private int nbColonnes = 0;
    
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS LIVRE" +
                                               "(id int auto_increment NOT NULL PRIMARY KEY, " +
                                               "titre VARCHAR(255), " +
                                               "auteur VARCHAR(255), " +
                                               "numero VARCHAR(255), " +
                                               "categorie VARCHAR(255), " +
                                               "emplacement VARCHAR(255), " +
                                               "image BLOB);";
    
    public GestionBaseDeDonnees() {
        this.ouvrirConnection();
    }
    
    private void ouvrirConnection() {
        try {
            Class.forName(GestionBaseDeDonnees.JDBC_DRIVER);
            this.connection = DriverManager.getConnection(GestionBaseDeDonnees.DB_URL, GestionBaseDeDonnees.DB_USERNAME, GestionBaseDeDonnees.DB_PASSWORD);
            this.statement = this.connection.createStatement();
            System.out.println("Connexion...");
            this.createTableLivre();
            System.out.println("Table crée.");
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void fermerConnection() {
        try {
            this.statement.close();
            this.connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createTableLivre() {
        try {
            this.statement.execute(GestionBaseDeDonnees.CREATE_TABLE);
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertIntoLivre(Livre livre) {
        try {
            String titre = livre.getTitre();
            String auteur = livre.getAuteur();
            String numero = livre.getNumero();
            String categorie = livre.getCategorie();
            String emplacement = livre.getEmplacement();
            String chemin = livre.getCheminImage();
            
            File file = null;
            FileInputStream fileInputStream = null;
            if (chemin != null) {
                file = new File(chemin);
                fileInputStream = new FileInputStream(file);
            }

            String sqlQuery = "INSERT INTO LIVRE (titre, auteur, numero, categorie, emplacement, image) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, titre);
            preparedStatement.setString(2, auteur);
            preparedStatement.setString(3, numero);
            preparedStatement.setString(4, categorie);
            preparedStatement.setString(5, emplacement);
            if (fileInputStream != null) {
                preparedStatement.setBinaryStream(6, fileInputStream, (int)file.length());
            }
            else {
                preparedStatement.setNull(6, Types.BLOB);
            }
            preparedStatement.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public java.util.ArrayList<Livre> selectAllFromLivre() {
        java.util.ArrayList<Livre> alLivres = new java.util.ArrayList();
        try {
            String sqlQuery = "SELECT * FROM LIVRE;";
            ResultSet resultSet = this.statement.executeQuery(sqlQuery);
            
            this.nbColonnes = resultSet.getMetaData().getColumnCount();
                    
            while (resultSet.next()) {
                Blob blob = resultSet.getBlob("image");
                BufferedImage image = null;
                try {
                    image = ImageIO.read(blob.getBinaryStream());
                } catch (IOException ex) {
                    Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
                }
                Livre livre = new Livre(resultSet.getString("id"), resultSet.getString("titre"), resultSet.getString("auteur"), resultSet.getString("numero"), resultSet.getString("categorie"), resultSet.getString("emplacement"), "", image);
                alLivres.add(livre);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alLivres;
    }
    
    public java.util.ArrayList<Object[]> selectAllObjectsFromLivre() {
        java.util.ArrayList<Object[]> alObjects = new java.util.ArrayList();
        try {
            String sqlQuery = "SELECT * FROM LIVRE;";
            ResultSet resultSet = this.statement.executeQuery(sqlQuery);
            
            this.nbColonnes = resultSet.getMetaData().getColumnCount();
            
            while (resultSet.next()) {
                Object[] objects = new Object[this.nbColonnes];
                for (int i = 1; i <= this.nbColonnes; i++) {
                    objects[i-1] = resultSet.getObject(i);
                }
                alObjects.add(objects);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alObjects;
    }
    
    public int selectCountAllFromLivre() {
        int res = 0;
        try {
            String sqlQuery = "SELECT COUNT(*) FROM LIVRE;";
            ResultSet resultSet = this.statement.executeQuery(sqlQuery);
            resultSet.next();
            res = resultSet.getInt(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public Object[] selectAllFromLivreWhereId(int id) {
        Object[] object = new Object[this.nbColonnes];
        try {
            String sqlQuery = "SELECT * FROM LIVRE WHERE ID = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (int i = 1; i <= this.nbColonnes - 1; i++) {
                    object[i-1] = resultSet.getObject(i);
                }
                Blob blob = resultSet.getBlob("image");
                BufferedImage image = null;
                if (blob != null) {
                    image = ImageIO.read(blob.getBinaryStream());
                }
                object[6] = image;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return object;
    }
    
    public BufferedImage selectImageFromLivreWhereId(int id) {
        BufferedImage image = null;
        try {
            String sqlQuery = "SELECT IMAGE FROM LIVRE WHERE ID = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Blob blob = resultSet.getBlob("image");
                if (blob != null) {
                    image = ImageIO.read(blob.getBinaryStream());
                }
                else {
                    image = null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    
    public java.util.ArrayList<Object[]> selectAllObjectsFromLivreWhereConcat(String concat) {
        String[] concatSplit = concat.split(" ");
        String sqlQuery = "";
        for (int i = 0; i < concatSplit.length; i++) {
            System.out.println(concatSplit[i]);
            if (i != 0) {
                sqlQuery += " INTERSECT ";
            }
            sqlQuery += "SELECT * FROM LIVRE WHERE UPPER(titre) LIKE '%" + concatSplit[i].toUpperCase() + "%' " +
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE UPPER(auteur) LIKE '%" + concatSplit[i].toUpperCase() + "%' " +
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE UPPER(numero) = '" + concatSplit[i].toUpperCase() + "' " +
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE UPPER(categorie) LIKE '%" + concatSplit[i].toUpperCase() + "%' " +            
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE UPPER(emplacement) LIKE '%" + concatSplit[i].toUpperCase() + "%'";
            System.out.println(sqlQuery);
        }
        java.util.ArrayList<Object[]> alObjects = new java.util.ArrayList();
        try {
            /*String sqlQuery = "SELECT * FROM LIVRE WHERE titre LIKE '%" + concat + "%' " +
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE auteur LIKE '%" + concat + "%' " +
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE numero LIKE '%" + concat + "%' " +
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE categorie LIKE '%" + concat + "%' " +            
                              "UNION " +
                              "SELECT * FROM LIVRE WHERE emplacement LIKE '%" + concat + "%' ";*/
                              
            //PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            //preparedStatement.setString(1, concat);
            //ResultSet resultSet = preparedStatement.executeQuery(sqlQuery);
            ResultSet resultSet = this.statement.executeQuery(sqlQuery);
            
            this.nbColonnes = resultSet.getMetaData().getColumnCount();
            
            while (resultSet.next()) {
                Object[] objects = new Object[this.nbColonnes];
                for (int i = 1; i <= this.nbColonnes; i++) {
                    objects[i-1] = resultSet.getObject(i);
                }
                alObjects.add(objects);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alObjects;
    }
    
    public void deleteAllFromLivre() {
        try {
            String sqlQuery = "DELETE FROM LIVRE;";
            this.statement.execute(sqlQuery);
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteLivreWhereId(Livre livre) {
        try {
            String id = livre.getId();
            String sqlQuery = "DELETE " +
                              "FROM LIVRE " +
                              "WHERE LIVRE.id = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, Integer.valueOf(id));
            preparedStatement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteLivreWhereId(int id) {
        try {
            String sqlQuery = "DELETE " +
                              "FROM LIVRE " +
                              "WHERE LIVRE.id = " + id + ";";
            this.statement.executeUpdate(sqlQuery);
            /*PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate(sqlQuery);*/
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateLivreWhereId(Livre livre) {
        try {
            File file = null;
            FileInputStream fileInputStream = null;
            if (livre.getCheminImage() != null) {
                file = new File(livre.getCheminImage());
                fileInputStream = new FileInputStream(file);
            }
            String sqlQuery = "UPDATE LIVRE " +
                              "SET titre = ?, " +
                              "auteur = ?, " +
                              "numero = ?, " +
                              "categorie = ?, " +
                              "emplacement = ?, " +
                              "image = ? " +
                              "WHERE id = ? ;";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, livre.getTitre());
            preparedStatement.setString(2, livre.getAuteur());
            preparedStatement.setString(3, livre.getNumero());
            preparedStatement.setString(4, livre.getCategorie());
            preparedStatement.setString(5, livre.getEmplacement());
            preparedStatement.setInt(7, Integer.valueOf(livre.getId()));
            if (fileInputStream != null) {
                preparedStatement.setBinaryStream(6, fileInputStream);
            }
            else if (livre.getImage() != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(livre.getImage(), "png", os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                preparedStatement.setBinaryStream(6, is);
            }
            else {
                preparedStatement.setNull(6, Types.BLOB);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dropTableLivre() {
        try {
            String sqlQuery = "DROP TABLE LIVRE;";
            this.statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            Logger.getLogger(GestionBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dropTableLivreAndRecreateTableLivre() {
        this.dropTableLivre();
        this.createTableLivre();
    }
    
    public int getNbColonne() {
        return this.nbColonnes;
    }

}
