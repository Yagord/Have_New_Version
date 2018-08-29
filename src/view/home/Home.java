package view.home;

import database.GestionBaseDeDonnees;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import model.Configurations;
import model.Livre;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pierre-Nicolas
 */
public class Home extends javax.swing.JFrame {
    private Configurations config = new Configurations();
    private static final String SEPARATOR = ";";
    private ArrayList<String> alCategories = new ArrayList();
    private ArrayList<String> alEmplacements = new ArrayList();
    private static final Color COLOR_BUTTON_UNSELECTED = new Color(24, 26, 31);
    private static final Color COLOR_BUTTON_SELECTED = new Color(228, 228, 229);
    private static final Color COLOR_LABEL_UNSELECTED = new Color(197, 56, 53);
    private static final Color COLOR_LABEL_SELECTED = new Color(181, 53, 51);
    private static final Color COLOR_BUTTON_CLOSE_UNSELECTED = new Color(24, 26, 31);
    private static final Color COLOR_BUTTON_CLOSE_SELECTED = new Color(203, 65, 62);
    private static final Color COLOR_LABEL_CLOSE_UNSELECTED = new Color(197, 56, 53);
    private static final Color COLOR_LABEL_CLOSE_SELECTED = new Color(255, 255, 255);
    private static final Color COLOR_SEPARATOR_UNSELECTED = new Color(197, 56, 53);
    private static final Color COLOR_SEPARATOR_SELECTED = new Color(228, 228, 229);
    private static final Color COLOR_BUTTON_DATA_UNSELECTED = new Color(203, 65, 62);
    private static final Color COLOR_BUTTON_DATA_SELECTED = new Color(197, 56, 53);
    private static final Color COLOR_LABEL_DATA_UNSELECTED = new Color(233, 233, 233);
    private static final Color COLOR_LABEL_DATA_SELECTED = new Color(251, 205, 63);
    private static final int LONGUEUR_LABEL_IMAGE_CENTRALE = 192;
    private static final int LARGEUR_LABEL_IMAGE_CENTRALE = 266;
    private static final int LONGUEUR_LABEL_IMAGE_APERCU = 60;
    private static final int LARGEUR_LABEL_IMAGE_APERCU = 83;
    private boolean consulterPressed;
    private boolean ajouterPressed;
    private boolean supprimerPressed;
    private boolean modifierPressed;
    private GestionBaseDeDonnees gestionBaseDeDonnees;
    private int posX;
    private int posY;
    private String cheminImage;
    private BufferedImage imageCentrale;
    
    private void initBooleanPressed() {
        this.consulterPressed = false;
        this.ajouterPressed = false;
        this.supprimerPressed = false;
        this.modifierPressed = false;
    }
    
    private void initCursor() {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void initGestionBaseDeDonnees() {
        this.gestionBaseDeDonnees = new GestionBaseDeDonnees();
        this.chargerDonnees();
    }
    
    private void initConfig() {
        File file = new File("ressources/config.properties");
        if (file.exists()) {
            this.config.chargerProperties();
            System.out.println("charge");
            String[] categories = this.config.getProperty("categorie").split(Home.SEPARATOR);
            String[] emplacements = this.config.getProperty("emplacement").split(Home.SEPARATOR);
            this.alCategories = new ArrayList(Arrays.asList(categories));
            this.alEmplacements = new ArrayList(Arrays.asList(emplacements));
            System.out.println(this.alCategories);
            System.out.println(this.alEmplacements);
        }
    }
    
    private void initComboBox() {
        this.textFieldNouveauCategorie.setVisible(false);
        this.textFieldNouveauEmplacement.setVisible(false);
        
        this.comboBoxCategorieAjouter.removeAllItems();
        this.comboBoxCategorieModifier.removeAllItems();
        this.comboBoxCategorieSupprimer.removeAllItems();
        this.comboBoxEmplacementAjouter.removeAllItems();
        this.comboBoxEmplacementModifier.removeAllItems();
        this.comboBoxEmplacementSupprimer.removeAllItems();
        
        for (int i = 0; i < this.alCategories.size(); i++) {
            this.comboBoxCategorieAjouter.addItem(this.alCategories.get(i));
            this.comboBoxCategorieModifier.addItem(this.alCategories.get(i));
            this.comboBoxCategorieSupprimer.addItem(this.alCategories.get(i));
        }
        for (int i = 0; i < this.alEmplacements.size(); i++) {
            this.comboBoxEmplacementAjouter.addItem(this.alEmplacements.get(i));
            this.comboBoxEmplacementModifier.addItem(this.alEmplacements.get(i));
            this.comboBoxEmplacementSupprimer.addItem(this.alEmplacements.get(i));
        }
    }
    
    private void initAffichageConsulter() {
        this.afficherConsulter();
    }
    
    private void initAffichageAjouter() {
        this.textFieldTitreAjouter.setText("");
        this.texteFieldAuteurAjouter.setText("");
        this.textFieldNumeroAjouter.setText("");
        this.comboBoxCategorieAjouter.setSelectedIndex(0);
        this.comboBoxEmplacementAjouter.setSelectedIndex(0);
        this.checkBoxNumAuto.setSelected(false);
        this.cheminImage = null;
        this.labelApercuImageAjouter.setIcon(null);
        this.labelCheminImageAjouter.setText("");
    }
    
    private void initAffichageSupprimer() {
        this.textFieldTitreSupprimer.setText("");
        this.texteFieldAuteurSupprimer.setText("");
        this.textFieldNumeroSupprimer.setText("");
        this.comboBoxCategorieSupprimer.setSelectedIndex(0);
        this.comboBoxEmplacementSupprimer.setSelectedIndex(0);
    }
        
    private void initAffichageModifier() {
        this.textFieldTitreModifier.setText("");
        this.texteFieldAuteurModifier.setText("");
        this.textFieldNumeroModifier.setText("");
        this.comboBoxCategorieModifier.setSelectedIndex(0);
        this.comboBoxEmplacementModifier.setSelectedIndex(0);
        this.cheminImage = null;
    }
    
    private void afficherConsulter() {
        this.initAffichageAjouter();
        this.initAffichageSupprimer();
        this.initAffichageModifier();
        
        this.panelConsulter.setVisible(true);
        this.panelAjouter.setVisible(false);
        this.panelSupprimer.setVisible(false);
        this.panelModifier.setVisible(false);
    }
    
    private void afficherAjouter() {
        this.viderImageCentrale();
        this.initAffichageSupprimer();
        this.initAffichageModifier();
        
        this.panelConsulter.setVisible(false);
        this.panelAjouter.setVisible(true);
        this.panelSupprimer.setVisible(false);
        this.panelModifier.setVisible(false);
        
        this.textFieldTitreAjouter.requestFocus();
    }
    
    private void afficherSupprimer() {
        this.viderImageCentrale();
        this.initAffichageAjouter();
        this.initAffichageModifier();
        
        this.panelConsulter.setVisible(false);
        this.panelAjouter.setVisible(false);
        this.panelSupprimer.setVisible(true);
        this.panelModifier.setVisible(false);
    }
        
    private void afficherModifier() {
        this.viderImageCentrale();
        this.initAffichageAjouter();
        this.initAffichageModifier();
        
        this.panelConsulter.setVisible(false);
        this.panelAjouter.setVisible(false);
        this.panelSupprimer.setVisible(false);
        this.panelModifier.setVisible(true);
        
        this.textFieldTitreModifier.requestFocus();
    }

    private void setColor(JPanel jPanel, Color color) {
        jPanel.setBackground(color);
    }
    
    private void setColor(JLabel jLabel, Color color) {
        jLabel.setForeground(color);
    }

    private boolean ligneSelectionne() {
        return !this.tableDatabase.getSelectionModel().isSelectionEmpty();
    }
    
    private int selectionnerIdLigne() {
        int indexLigne = this.tableDatabase.getSelectedRow();
        int id = (int)this.tableDatabase.getModel().getValueAt(indexLigne, 0);
        return id;
    }
    
    private Object[] selectionnerLigne() {
        return this.gestionBaseDeDonnees.selectAllFromLivreWhereId(this.selectionnerIdLigne());
    }
    
    private ImageIcon afficherNoImage() {
        String cheminNoImage = "images/noImageAvailable.png";
        return new ImageIcon(this.getClass().getClassLoader().getResource(cheminNoImage));
    }
    
    private void chargerUneImage(BufferedImage bufferedImage, JLabel label, int longueur, int largeur) {
        ImageIcon imageIcon = null;
        if (bufferedImage != null) {
            imageIcon = new ImageIcon(bufferedImage);
            if (label.equals(this.labelImageCentrale)) {
                this.imageCentrale = bufferedImage;
            }
        }
        else {
            imageIcon = this.afficherNoImage();
        }
        Image newImage = imageIcon.getImage().getScaledInstance(longueur, largeur, java.awt.Image.SCALE_SMOOTH);
        imageIcon.setImage(newImage);
        label.setIcon(imageIcon);
    }
    
    private void chargerUneImageCentrale(BufferedImage bufferedImage) {
        this.chargerUneImage(bufferedImage, this.labelImageCentrale, Home.LONGUEUR_LABEL_IMAGE_CENTRALE, Home.LARGEUR_LABEL_IMAGE_CENTRALE);
    }
    
    private void chargerUneImageApercu() {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(this.cheminImage));
            this.chargerUneImage(bufferedImage, this.labelApercuImageAjouter, Home.LONGUEUR_LABEL_IMAGE_APERCU, Home.LARGEUR_LABEL_IMAGE_APERCU);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void viderImageCentrale() {
        this.labelImageCentrale.setIcon(null);
    }
    
    private boolean chercherUneImage() {
        boolean res = false;
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        int result = jFileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            this.cheminImage = jFileChooser.getSelectedFile().getPath();
            res = true;
        }
        //FileDialog fileDialog = new FileDialog(this);
        //fileDialog.setVisible(true);
        //System.out.println(fileDialog.getFile());
        //System.out.println(fileDialog.getDirectory());
        return res;
    }
        
    private void chargerDonnees() {
        java.util.ArrayList<Object[]> alLivres = new java.util.ArrayList();
        alLivres = this.gestionBaseDeDonnees.selectAllObjectsFromLivre();
        
        while (this.tableDatabase.getRowCount() > 0) {
            ((DefaultTableModel)this.tableDatabase.getModel()).removeRow(0);
        }
        for (int i = 0; i < alLivres.size(); i++) {
            ((DefaultTableModel)this.tableDatabase.getModel()).insertRow(i, alLivres.get(i));
        }
    }
    
    private void ajouterDonnees() {
        String titre = this.textFieldTitreAjouter.getText();
        String auteur = this.texteFieldAuteurAjouter.getText();
        String numero = this.textFieldNumeroAjouter.getText();
        String categorie = this.comboBoxCategorieAjouter.getSelectedItem().toString();
        String emplacement = this.comboBoxEmplacementAjouter.getSelectedItem().toString();
        
        if (titre.equals("") || auteur.equals("") || numero.equals("") || categorie.equals("") || emplacement.equals("") || this.cheminImage == null) {
            int reponse = JOptionPane.showConfirmDialog(this, "Vous ne remplissez pas tous les champs ?\nSouhaitez-vous enregistrer ce livre ?","Confirmation d'Enregistrement.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (reponse == JOptionPane.OK_OPTION) {
                Livre livre = new Livre("", titre, auteur, numero, categorie, emplacement, this.cheminImage, null);
                this.gestionBaseDeDonnees.insertIntoLivre(livre);
            }
        }
        else {
            Livre livre = new Livre("", titre, auteur, numero, categorie, emplacement, this.cheminImage, null);
            this.gestionBaseDeDonnees.insertIntoLivre(livre);
        }
    }
    
    private void supprimerDonnees() {
        if (this.ligneSelectionne()) {
            this.gestionBaseDeDonnees.deleteLivreWhereId(this.selectionnerIdLigne());
            this.viderImageCentrale();
            this.panelButtonConsulterMousePressed();
        }
    }
    
    private void modifierDonnees() {
        if (this.ligneSelectionne()) {
            int id = this.selectionnerIdLigne();
            String titre = this.textFieldTitreModifier.getText();
            String auteur = this.texteFieldAuteurModifier.getText();
            String numero = this.textFieldNumeroModifier.getText();
            String categorie = this.comboBoxCategorieModifier.getSelectedItem().toString();
            String emplacement = this.comboBoxEmplacementModifier.getSelectedItem().toString();
            
            Livre livre = new Livre(String.valueOf(id), titre, auteur, numero, categorie, emplacement, this.cheminImage, this.imageCentrale);
            this.gestionBaseDeDonnees.updateLivreWhereId(livre);
            this.viderImageCentrale();
            this.panelButtonConsulterMousePressed();
        }
    }

    private void panelButtonCloseMousePressed() {                                              
        this.setColor(this.panelButtonClose, COLOR_BUTTON_CLOSE_SELECTED);
        this.setColor(this.labelClose, COLOR_LABEL_CLOSE_SELECTED);
        this.gestionBaseDeDonnees.fermerConnection();
        this.dispose();
    }                                             

    private void panelButtonCloseMouseExited() {                                             
        this.setColor(this.panelButtonClose, COLOR_BUTTON_CLOSE_UNSELECTED);
        this.setColor(this.labelClose, COLOR_LABEL_CLOSE_UNSELECTED);
    }                                            

    private void panelButtonCloseMouseEntered() {                                              
        this.setColor(this.panelButtonClose, COLOR_BUTTON_CLOSE_SELECTED);
        this.setColor(this.labelClose, COLOR_LABEL_CLOSE_SELECTED);
    }
    
    private void panelButtonModifierMousePressed() {
        this.afficherModifier();
        this.consulterPressed = false;
        this.ajouterPressed = false;
        this.supprimerPressed = false;
        this.modifierPressed = true;

        this.setColor(this.panelButtonConsulter, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonAjouter, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonSupprimer, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonModifier, Home.COLOR_BUTTON_SELECTED);
        
        this.setColor(this.panelHideSeparatorConsulter, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorAjouter, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorSupprimer, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorModifier, Home.COLOR_SEPARATOR_SELECTED);
        
        this.setColor(this.labelConsulter, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelAjouter, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelSupprimer, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelModifier, Home.COLOR_LABEL_SELECTED);        
    }
    
    private void panelButtonModifierMouseEntered() {
        if (!this.modifierPressed) {
            this.setColor(this.panelButtonModifier, Home.COLOR_BUTTON_SELECTED);
            this.setColor(this.labelModifier, Home.COLOR_LABEL_SELECTED);
            this.setColor(this.panelHideSeparatorModifier, Home.COLOR_SEPARATOR_SELECTED);
        }
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void panelButtonModifierMouseExited() {
        if (!this.modifierPressed) {
            this.setColor(this.panelButtonModifier, Home.COLOR_BUTTON_UNSELECTED);
            this.setColor(this.labelModifier, Home.COLOR_LABEL_UNSELECTED);
            this.setColor(this.panelHideSeparatorModifier, Home.COLOR_SEPARATOR_UNSELECTED);
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void panelButtonSupprimerMousePressed() {
        this.afficherSupprimer();

        this.consulterPressed = false;
        this.ajouterPressed = false;
        this.supprimerPressed = true;
        this.modifierPressed = false;

        this.setColor(this.panelButtonConsulter, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonAjouter, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonSupprimer, Home.COLOR_BUTTON_SELECTED);
        this.setColor(this.panelButtonModifier, Home.COLOR_BUTTON_UNSELECTED);
        
        this.setColor(this.panelHideSeparatorConsulter, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorAjouter, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorSupprimer, Home.COLOR_SEPARATOR_SELECTED);
        this.setColor(this.panelHideSeparatorModifier, Home.COLOR_SEPARATOR_UNSELECTED);

        this.setColor(this.labelConsulter, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelAjouter, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelSupprimer, Home.COLOR_LABEL_SELECTED);
        this.setColor(this.labelModifier, Home.COLOR_LABEL_UNSELECTED);        
    }
    
    private void panelButtonSupprimerMouseEntered() {
        if (!this.supprimerPressed) {
            this.setColor(this.panelButtonSupprimer, Home.COLOR_BUTTON_SELECTED);
            this.setColor(this.labelSupprimer, Home.COLOR_LABEL_SELECTED);
            this.setColor(this.panelHideSeparatorSupprimer, Home.COLOR_SEPARATOR_SELECTED);
        }
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));        
    }
    
    private void panelButtonSupprimerMouseExited() {
        if (!this.supprimerPressed) {
            this.setColor(this.panelButtonSupprimer, Home.COLOR_BUTTON_UNSELECTED);
            this.setColor(this.labelSupprimer, Home.COLOR_LABEL_UNSELECTED);
            this.setColor(this.panelHideSeparatorSupprimer, Home.COLOR_SEPARATOR_UNSELECTED);
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void panelButtonAjouterMousePressed() {
        this.afficherAjouter();

        this.consulterPressed = false;
        this.ajouterPressed = true;
        this.supprimerPressed = false;
        this.modifierPressed = false;

        this.setColor(this.panelButtonConsulter, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonAjouter, Home.COLOR_BUTTON_SELECTED);
        this.setColor(this.panelButtonSupprimer, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonModifier, Home.COLOR_BUTTON_UNSELECTED);
        
        this.setColor(this.panelHideSeparatorConsulter, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorAjouter, Home.COLOR_SEPARATOR_SELECTED);
        this.setColor(this.panelHideSeparatorSupprimer, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorModifier, Home.COLOR_SEPARATOR_UNSELECTED);

        this.setColor(this.labelConsulter, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelAjouter, Home.COLOR_LABEL_SELECTED);
        this.setColor(this.labelSupprimer, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelModifier, Home.COLOR_LABEL_UNSELECTED);        
    }
    
    private void panelButtonAjouterMouseEntered() {
        if (!this.ajouterPressed) {
            this.setColor(this.panelButtonAjouter, Home.COLOR_BUTTON_SELECTED);
            this.setColor(this.labelAjouter, Home.COLOR_LABEL_SELECTED);
            this.setColor(this.panelHideSeparatorAjouter, Home.COLOR_SEPARATOR_SELECTED);
        }        
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void panelButtonAjouterMouseExited() {
        if (!this.ajouterPressed) {
            this.setColor(this.panelButtonAjouter, Home.COLOR_BUTTON_UNSELECTED);
            this.setColor(this.labelAjouter, Home.COLOR_LABEL_UNSELECTED);
            this.setColor(this.panelHideSeparatorAjouter, Home.COLOR_SEPARATOR_UNSELECTED);
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void panelButtonConsulterMousePressed() {
        this.chargerDonnees();

        this.afficherConsulter();

        this.consulterPressed = true;
        this.ajouterPressed = false;
        this.supprimerPressed = false;
        this.modifierPressed = false;

        this.setColor(this.panelButtonConsulter, Home.COLOR_BUTTON_SELECTED);
        this.setColor(this.panelButtonAjouter, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonSupprimer, Home.COLOR_BUTTON_UNSELECTED);
        this.setColor(this.panelButtonModifier, Home.COLOR_BUTTON_UNSELECTED);
        
        this.setColor(this.panelHideSeparatorConsulter, Home.COLOR_SEPARATOR_SELECTED);
        this.setColor(this.panelHideSeparatorAjouter, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorSupprimer, Home.COLOR_SEPARATOR_UNSELECTED);
        this.setColor(this.panelHideSeparatorModifier, Home.COLOR_SEPARATOR_UNSELECTED);

        this.setColor(this.labelConsulter, Home.COLOR_LABEL_SELECTED);
        this.setColor(this.labelAjouter, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelSupprimer, Home.COLOR_LABEL_UNSELECTED);
        this.setColor(this.labelModifier, Home.COLOR_LABEL_UNSELECTED);        
    }
    
    private void panelButtonConsulterMouseEntered() {
        if (!this.consulterPressed) {
            this.setColor(this.panelButtonConsulter, Home.COLOR_BUTTON_SELECTED);
            this.setColor(this.labelConsulter, Home.COLOR_LABEL_SELECTED);
            this.setColor(this.panelHideSeparatorConsulter, Home.COLOR_SEPARATOR_SELECTED);
        }        
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void panelButtonConsulterMouseExited() {
        if (!this.consulterPressed) {
            this.setColor(this.panelButtonConsulter, Home.COLOR_BUTTON_UNSELECTED);
            this.setColor(this.labelConsulter, Home.COLOR_LABEL_UNSELECTED);
            this.setColor(this.panelHideSeparatorConsulter, Home.COLOR_SEPARATOR_UNSELECTED);
        }        
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    private void panelAjouterUnLivreMousePressed() {                                                 
        this.panelButtonAjouterMousePressed();
    }                                                

    private void panelAjouterUnLivreMouseEntered() {                                                 
        this.setColor(this.panelAjouterUnLivre, Home.COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelAjouterUnLivre, Home.COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                

    private void panelAjouterUnLivreMouseExited() {                                                
        this.setColor(this.panelAjouterUnLivre, Home.COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelAjouterUnLivre, Home.COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                               

    private void panelSupprimerUnLivreMousePressed() {                                                   
        this.panelButtonSupprimerMousePressed();
        
        if (this.ligneSelectionne()) {                  //Une ligne est forcéméent sélectionnée
            Object[] object = this.selectionnerLigne();
            this.textFieldTitreSupprimer.setText((String) object[1]);
            this.texteFieldAuteurSupprimer.setText((String) object[2]);
            this.textFieldNumeroSupprimer.setText((String) object[3]);
            this.comboBoxEmplacementSupprimer.setSelectedItem(object[4]);
            this.comboBoxEmplacementSupprimer.setSelectedItem(object[5]);
            this.chargerUneImageCentrale((BufferedImage) object[6]);
        }
    }                            

    private void panelSupprimerUnLivreMouseEntered() {                                                   
        this.setColor(this.panelSupprimerUnLivre, Home.COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelSupprimerUnLivre, Home.COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                  

    private void panelSupprimerUnLivreMouseExited() {                                                  
        this.setColor(this.panelSupprimerUnLivre, Home.COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelSupprimerUnLivre, Home.COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                                 

    private void panelModifierUnLivreMousePressed() {                                                  
        this.panelButtonModifierMousePressed();
        
        if (this.ligneSelectionne()) {                  //Une ligne est forcéméent sélectionnée
            Object[] object = this.selectionnerLigne();
            this.textFieldTitreModifier.setText((String) object[1]);
            this.texteFieldAuteurModifier.setText((String) object[2]);
            this.textFieldNumeroModifier.setText((String) object[3]);
            this.comboBoxEmplacementModifier.setSelectedItem(object[4]);
            this.comboBoxEmplacementModifier.setSelectedItem(object[5]);
            this.chargerUneImageCentrale((BufferedImage) object[6]);
        }
    }                                                 

    private void panelModifierUnLivreMouseEntered() {                                                  
        this.setColor(this.panelModifierUnLivre, Home.COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelModifierUnLivre, Home.COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                 

    private void panelModifierUnLivreMouseExited() {                                                 
        this.setColor(this.panelModifierUnLivre, Home.COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelModifierUnLivre, Home.COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void panelAjouterCeLivreMouseEntered() {                                                 
        this.setColor(this.panelAjouterCeLivre, Home.COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelAjouterCeLivre, Home.COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                

    private void panelAjouterCeLivreMouseExited() {                                                
        this.setColor(this.panelAjouterCeLivre, Home.COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelAjouterCeLivre, Home.COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                               

    private void panelAjouterCeLivreMousePressed() {                                                 
        this.ajouterDonnees();
        if (!this.checkBoxNumAuto.isSelected()) {
            this.initAffichageAjouter();
        }
        else {
            this.textFieldNumeroAjouter.getText();
            if (!this.textFieldNumeroAjouter.getText().equals("")) {
                this.textFieldNumeroAjouter.setText("" + (Integer.valueOf(this.textFieldNumeroAjouter.getText()) + 1));

            }
            this.cheminImage = null;
        }
    }                                                

    private void panelSupprimerCeLivreMouseEntered() {                                                   
        this.setColor(this.panelSupprimerCeLivre, Home.COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelSupprimerCeLivre, Home.COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                  

    private void panelSupprimerCeLivreMouseExited() {                                                  
        this.setColor(this.panelSupprimerCeLivre, Home.COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelSupprimerCeLivre, Home.COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                                 

    private void panelSupprimerCeLivreMousePressed() {                                                   
        this.supprimerDonnees();
    }                                                  

    private void paneModifierCeLivreMouseEntered() {                                                 
        this.setColor(this.panelModifierCeLivre, Home.COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelModifierCeLivre, Home.COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                

    private void paneModifierCeLivreMouseExited() {                                                
        this.setColor(this.panelModifierCeLivre, Home.COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelModifierCeLivre, Home.COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                               

    private void paneModifierCeLivreMousePressed() {                                                 
        this.modifierDonnees();
    }
    
    private void tableDatabaseMousePressed() {                                           
        int idSelectedRow = this.selectionnerIdLigne();
        BufferedImage bufferedImage = this.gestionBaseDeDonnees.selectImageFromLivreWhereId(idSelectedRow);
        this.chargerUneImageCentrale(bufferedImage);
    }                                          

    private void panelParcourirAjouterMouseEntered() {                                                   
        this.setColor(this.panelParcourirAjouter, COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelParcourirAjouter, COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                  

    private void panelParcourirAjouterMouseExited() {                                                  
        this.setColor(this.panelParcourirAjouter, COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelParcourirAjouter, COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                                 

    private void panelParcourirAjouterMousePressed() {                                                   
        boolean imageSelectionne = this.chercherUneImage();
        if (imageSelectionne) {
            this.chargerUneImageApercu();
            this.labelCheminImageAjouter.setText(this.cheminImage);
        }
    }                                                                                                

    private void panelParcourirModifierMouseEntered() {                                                    
        this.setColor(this.panelParcourirModifier, COLOR_BUTTON_DATA_SELECTED);
        this.setColor(this.labelParcourirModifier, COLOR_LABEL_DATA_SELECTED);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }                                                   

    private void panelParcourirModifierMouseExited() {                                                   
        this.setColor(this.panelParcourirModifier, COLOR_BUTTON_DATA_UNSELECTED);
        this.setColor(this.labelParcourirModifier, COLOR_LABEL_DATA_UNSELECTED);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }                                                  

    private void panelParcourirModifierMousePressed() {                                                    
        this.chercherUneImage();
    }
    
    private void labelAddCategorieAjouterMousePressed() {
        this.textFieldNouveauCategorie.setVisible(true);
        this.labelAddCategorieAjouter.setVisible(false);
        this.textFieldNouveauCategorie.requestFocus();
    }                                                     

    private void labelAddEmplacementAjouterMousePressed() {
        this.textFieldNouveauEmplacement.setVisible(true);
        this.labelAddEmplacementAjouter.setVisible(false);
        this.textFieldNouveauEmplacement.requestFocus();
    }
    
    private void textFieldNouveauCategorieKeyPressed() {                                                     
        this.alCategories.add(this.textFieldNouveauCategorie.getText());
        String joinCategories = String.join(SEPARATOR, alCategories);
        this.config.ajouterProperty("categorie", joinCategories);
        this.config.sauvegarderProperties();
        this.initComboBox();
        this.textFieldNouveauCategorie.setText("");
        this.textFieldNouveauCategorie.setVisible(false);
        this.labelAddCategorieAjouter.setVisible(true);
    }                                                    

    private void textFieldNouveauEmplacementKeyPressed() {                                                       
        this.alEmplacements.add(this.textFieldNouveauEmplacement.getText());
        String joinEmplacements = String.join(SEPARATOR, alEmplacements);
        this.config.ajouterProperty("emplacement", joinEmplacements);
        this.config.sauvegarderProperties();
        this.initComboBox();
        this.textFieldNouveauEmplacement.setText("");
        this.textFieldNouveauEmplacement.setVisible(false);
        this.labelAddEmplacementAjouter.setVisible(true);
    }
    
    private void panelAjouterMouseClicked() {                                          
        this.textFieldNouveauCategorie.setText("");
        this.textFieldNouveauCategorie.setVisible(false);
        this.labelAddCategorieAjouter.setVisible(true);
        this.textFieldNouveauEmplacement.setText("");
        this.textFieldNouveauEmplacement.setVisible(false);
        this.labelAddEmplacementAjouter.setVisible(true);
    }
    
    private void textFieldSearchKeyPressed() {                                                
        this.labelSearchMousePressed();
    }                                               

    private void labelSearchMousePressed() {                                         
        String searchRequest = this.textFieldSearch.getText();
        java.util.ArrayList<Object[]> alLivres = new java.util.ArrayList();
        alLivres = this.gestionBaseDeDonnees.selectAllObjectsFromLivreWhereConcat(searchRequest);
        
        while (this.tableDatabase.getRowCount() > 0) {
            ((DefaultTableModel)this.tableDatabase.getModel()).removeRow(0);
        }
        for (int i = 0; i < alLivres.size(); i++) {
            ((DefaultTableModel)this.tableDatabase.getModel()).insertRow(i, alLivres.get(i));
        }        
    }
    
    /**
     * Creates new form Home
     */
    public Home() {
        this.initComponents();
        this.initCursor();
        this.initBooleanPressed();
        this.initGestionBaseDeDonnees();
        this.initConfig();
        this.initComboBox();
        this.initAffichageConsulter();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMain = new javax.swing.JPanel();
        panelPane = new javax.swing.JPanel();
        panelSeparator = new javax.swing.JPanel();
        panelHave = new javax.swing.JPanel();
        labelHave = new javax.swing.JLabel();
        panelButtonConsulter = new javax.swing.JPanel();
        labelConsulter = new javax.swing.JLabel();
        panelHideSeparatorConsulter = new javax.swing.JPanel();
        panelButtonAjouter = new javax.swing.JPanel();
        labelAjouter = new javax.swing.JLabel();
        panelHideSeparatorAjouter = new javax.swing.JPanel();
        panelButtonSupprimer = new javax.swing.JPanel();
        labelSupprimer = new javax.swing.JLabel();
        panelHideSeparatorSupprimer = new javax.swing.JPanel();
        panelButtonModifier = new javax.swing.JPanel();
        labelModifier = new javax.swing.JLabel();
        panelHideSeparatorModifier = new javax.swing.JPanel();
        panelData = new javax.swing.JPanel();
        panelConsulter = new javax.swing.JPanel();
        scrollpaneDatabase = new javax.swing.JScrollPane();
        tableDatabase = new javax.swing.JTable();
        panelAjouter = new javax.swing.JPanel();
        panelAjouterCeLivre = new javax.swing.JPanel();
        labelAjouterCeLivre = new javax.swing.JLabel();
        labelTitreAjouter = new javax.swing.JLabel();
        textFieldTitreAjouter = new javax.swing.JTextField();
        labelAuteurAjouter = new javax.swing.JLabel();
        texteFieldAuteurAjouter = new javax.swing.JTextField();
        labelNumeroAjouter = new javax.swing.JLabel();
        textFieldNumeroAjouter = new javax.swing.JTextField();
        labelCategorieAjouter = new javax.swing.JLabel();
        comboBoxCategorieAjouter = new javax.swing.JComboBox<>();
        labelEmplacementAjouter = new javax.swing.JLabel();
        comboBoxEmplacementAjouter = new javax.swing.JComboBox<>();
        labelAjoutLogo1 = new javax.swing.JLabel();
        labelAjoutLogo2 = new javax.swing.JLabel();
        labelAjoutLogo3 = new javax.swing.JLabel();
        panelParcourirAjouter = new javax.swing.JPanel();
        labelParcourirAjouter = new javax.swing.JLabel();
        labelImageAjouter = new javax.swing.JLabel();
        checkBoxNumAuto = new javax.swing.JCheckBox();
        labelApercuImageAjouter = new javax.swing.JLabel();
        labelCheminImageAjouter = new javax.swing.JLabel();
        labelAddCategorieAjouter = new javax.swing.JLabel();
        labelAddEmplacementAjouter = new javax.swing.JLabel();
        textFieldNouveauEmplacement = new javax.swing.JTextField();
        textFieldNouveauCategorie = new javax.swing.JTextField();
        panelSupprimer = new javax.swing.JPanel();
        panelSupprimerCeLivre = new javax.swing.JPanel();
        labelSupprimerCeLivre = new javax.swing.JLabel();
        labelTitreSupprimer = new javax.swing.JLabel();
        textFieldTitreSupprimer = new javax.swing.JTextField();
        labelAuteurSupprimer = new javax.swing.JLabel();
        texteFieldAuteurSupprimer = new javax.swing.JTextField();
        labelNumeroSupprimer = new javax.swing.JLabel();
        textFieldNumeroSupprimer = new javax.swing.JTextField();
        labelCategorieSupprimer = new javax.swing.JLabel();
        comboBoxCategorieSupprimer = new javax.swing.JComboBox<>();
        labelEmplacementSupprimer = new javax.swing.JLabel();
        comboBoxEmplacementSupprimer = new javax.swing.JComboBox<>();
        labelsuppressionLogo1 = new javax.swing.JLabel();
        labelsuppressionLogo2 = new javax.swing.JLabel();
        labelsuppressionLogo3 = new javax.swing.JLabel();
        panelModifier = new javax.swing.JPanel();
        panelModifierCeLivre = new javax.swing.JPanel();
        labelModifierCeLivre = new javax.swing.JLabel();
        labelTitreModifier = new javax.swing.JLabel();
        textFieldTitreModifier = new javax.swing.JTextField();
        labelAuteurModifier = new javax.swing.JLabel();
        texteFieldAuteurModifier = new javax.swing.JTextField();
        labelNumeroModifier = new javax.swing.JLabel();
        textFieldNumeroModifier = new javax.swing.JTextField();
        labelCategorieModifier = new javax.swing.JLabel();
        labelEmplacementModifier = new javax.swing.JLabel();
        labelModifiactionLogo1 = new javax.swing.JLabel();
        labelModifiactionLogo2 = new javax.swing.JLabel();
        labelModifiactionLogo3 = new javax.swing.JLabel();
        comboBoxCategorieModifier = new javax.swing.JComboBox<>();
        comboBoxEmplacementModifier = new javax.swing.JComboBox<>();
        panelParcourirModifier = new javax.swing.JPanel();
        labelParcourirModifier = new javax.swing.JLabel();
        labelImageModifier = new javax.swing.JLabel();
        panelButtonClose = new javax.swing.JPanel();
        labelClose = new javax.swing.JLabel();
        panelBar = new javax.swing.JPanel();
        panelAjouterUnLivre = new javax.swing.JPanel();
        labelAjouterUnLivre = new javax.swing.JLabel();
        panelSupprimerUnLivre = new javax.swing.JPanel();
        labelSupprimerUnLivre = new javax.swing.JLabel();
        panelModifierUnLivre = new javax.swing.JPanel();
        labelModifierUnLivre = new javax.swing.JLabel();
        labelImageCentrale = new javax.swing.JLabel();
        labelSearch = new javax.swing.JLabel();
        textFieldSearch = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setUndecorated(true);

        panelMain.setBackground(new java.awt.Color(24, 26, 31));
        panelMain.setPreferredSize(new java.awt.Dimension(1202, 650));
        panelMain.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelMainMouseDragged(evt);
            }
        });
        panelMain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelMainMousePressed(evt);
            }
        });
        panelMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelPane.setBackground(new java.awt.Color(24, 26, 31));
        panelPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSeparator.setBackground(new java.awt.Color(197, 56, 53));
        panelSeparator.setPreferredSize(new java.awt.Dimension(5, 650));

        javax.swing.GroupLayout panelSeparatorLayout = new javax.swing.GroupLayout(panelSeparator);
        panelSeparator.setLayout(panelSeparatorLayout);
        panelSeparatorLayout.setHorizontalGroup(
            panelSeparatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelSeparatorLayout.setVerticalGroup(
            panelSeparatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        panelPane.add(panelSeparator, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 0, 5, 650));

        panelHave.setBackground(new java.awt.Color(24, 26, 31));
        panelHave.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelHave.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelHave.setForeground(new java.awt.Color(197, 56, 53));
        labelHave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelHave.setText("HAVE");
        panelHave.add(labelHave, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 70));

        panelPane.add(panelHave, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 180, 70));

        panelButtonConsulter.setBackground(new java.awt.Color(24, 26, 31));
        panelButtonConsulter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelButtonConsulterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelButtonConsulterMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelButtonConsulterMousePressed(evt);
            }
        });
        panelButtonConsulter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelConsulter.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        labelConsulter.setForeground(new java.awt.Color(197, 56, 53));
        labelConsulter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelConsulter.setText("Consulter");
        panelButtonConsulter.add(labelConsulter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 40));

        panelHideSeparatorConsulter.setBackground(new java.awt.Color(197, 56, 53));

        javax.swing.GroupLayout panelHideSeparatorConsulterLayout = new javax.swing.GroupLayout(panelHideSeparatorConsulter);
        panelHideSeparatorConsulter.setLayout(panelHideSeparatorConsulterLayout);
        panelHideSeparatorConsulterLayout.setHorizontalGroup(
            panelHideSeparatorConsulterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        panelHideSeparatorConsulterLayout.setVerticalGroup(
            panelHideSeparatorConsulterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        panelButtonConsulter.add(panelHideSeparatorConsulter, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 0, -1, -1));

        panelPane.add(panelButtonConsulter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 280, 40));

        panelButtonAjouter.setBackground(new java.awt.Color(24, 26, 31));
        panelButtonAjouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelButtonAjouterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelButtonAjouterMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelButtonAjouterMousePressed(evt);
            }
        });
        panelButtonAjouter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelAjouter.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        labelAjouter.setForeground(new java.awt.Color(197, 56, 53));
        labelAjouter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAjouter.setText("Ajouter");
        panelButtonAjouter.add(labelAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 40));

        panelHideSeparatorAjouter.setBackground(new java.awt.Color(197, 56, 53));

        javax.swing.GroupLayout panelHideSeparatorAjouterLayout = new javax.swing.GroupLayout(panelHideSeparatorAjouter);
        panelHideSeparatorAjouter.setLayout(panelHideSeparatorAjouterLayout);
        panelHideSeparatorAjouterLayout.setHorizontalGroup(
            panelHideSeparatorAjouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        panelHideSeparatorAjouterLayout.setVerticalGroup(
            panelHideSeparatorAjouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        panelButtonAjouter.add(panelHideSeparatorAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 0, -1, -1));

        panelPane.add(panelButtonAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 280, 40));

        panelButtonSupprimer.setBackground(new java.awt.Color(24, 26, 31));
        panelButtonSupprimer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelButtonSupprimerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelButtonSupprimerMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelButtonSupprimerMousePressed(evt);
            }
        });
        panelButtonSupprimer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelSupprimer.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        labelSupprimer.setForeground(new java.awt.Color(197, 56, 53));
        labelSupprimer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSupprimer.setText("Supprimer");
        panelButtonSupprimer.add(labelSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 40));

        panelHideSeparatorSupprimer.setBackground(new java.awt.Color(197, 56, 53));

        javax.swing.GroupLayout panelHideSeparatorSupprimerLayout = new javax.swing.GroupLayout(panelHideSeparatorSupprimer);
        panelHideSeparatorSupprimer.setLayout(panelHideSeparatorSupprimerLayout);
        panelHideSeparatorSupprimerLayout.setHorizontalGroup(
            panelHideSeparatorSupprimerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        panelHideSeparatorSupprimerLayout.setVerticalGroup(
            panelHideSeparatorSupprimerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        panelButtonSupprimer.add(panelHideSeparatorSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 0, -1, -1));

        panelPane.add(panelButtonSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 280, 40));

        panelButtonModifier.setBackground(new java.awt.Color(24, 26, 31));
        panelButtonModifier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelButtonModifierMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelButtonModifierMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelButtonModifierMousePressed(evt);
            }
        });
        panelButtonModifier.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelModifier.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        labelModifier.setForeground(new java.awt.Color(197, 56, 53));
        labelModifier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelModifier.setText("Modifier");
        panelButtonModifier.add(labelModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 280, 40));

        panelHideSeparatorModifier.setBackground(new java.awt.Color(197, 56, 53));

        javax.swing.GroupLayout panelHideSeparatorModifierLayout = new javax.swing.GroupLayout(panelHideSeparatorModifier);
        panelHideSeparatorModifier.setLayout(panelHideSeparatorModifierLayout);
        panelHideSeparatorModifierLayout.setHorizontalGroup(
            panelHideSeparatorModifierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        panelHideSeparatorModifierLayout.setVerticalGroup(
            panelHideSeparatorModifierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        panelButtonModifier.add(panelHideSeparatorModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 0, -1, -1));

        panelPane.add(panelButtonModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 280, 40));

        panelMain.add(panelPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelData.setBackground(new java.awt.Color(242, 244, 247));
        panelData.setMinimumSize(new java.awt.Dimension(600, 500));
        panelData.setPreferredSize(new java.awt.Dimension(650, 525));
        panelData.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelConsulter.setPreferredSize(new java.awt.Dimension(650, 525));
        panelConsulter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrollpaneDatabase.setBorder(null);
        scrollpaneDatabase.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        scrollpaneDatabase.setPreferredSize(new java.awt.Dimension(650, 525));

        tableDatabase.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tableDatabase.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Titre", "Auteur", "Numero", "Categorie", "Emplacement"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDatabase.setGridColor(new java.awt.Color(255, 255, 255));
        tableDatabase.setSelectionBackground(new java.awt.Color(203, 65, 62));
        tableDatabase.setSelectionForeground(new java.awt.Color(233, 233, 233));
        tableDatabase.getTableHeader().setReorderingAllowed(false);
        tableDatabase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableDatabaseMousePressed(evt);
            }
        });
        scrollpaneDatabase.setViewportView(tableDatabase);
        if (tableDatabase.getColumnModel().getColumnCount() > 0) {
            tableDatabase.getColumnModel().getColumn(0).setResizable(false);
            tableDatabase.getColumnModel().getColumn(1).setResizable(false);
            tableDatabase.getColumnModel().getColumn(2).setResizable(false);
            tableDatabase.getColumnModel().getColumn(3).setResizable(false);
            tableDatabase.getColumnModel().getColumn(4).setResizable(false);
            tableDatabase.getColumnModel().getColumn(5).setResizable(false);
        }

        panelConsulter.add(scrollpaneDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 525));

        panelData.add(panelConsulter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 520));

        panelAjouter.setForeground(new java.awt.Color(24, 26, 31));
        panelAjouter.setPreferredSize(new java.awt.Dimension(650, 525));
        panelAjouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelAjouterMouseClicked(evt);
            }
        });
        panelAjouter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelAjouterCeLivre.setBackground(new java.awt.Color(203, 65, 62));
        panelAjouterCeLivre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelAjouterCeLivreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelAjouterCeLivreMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelAjouterCeLivreMousePressed(evt);
            }
        });

        labelAjouterCeLivre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelAjouterCeLivre.setForeground(new java.awt.Color(233, 233, 233));
        labelAjouterCeLivre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAjouterCeLivre.setText("Ajouter ce livre");

        javax.swing.GroupLayout panelAjouterCeLivreLayout = new javax.swing.GroupLayout(panelAjouterCeLivre);
        panelAjouterCeLivre.setLayout(panelAjouterCeLivreLayout);
        panelAjouterCeLivreLayout.setHorizontalGroup(
            panelAjouterCeLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjouterCeLivreLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelAjouterCeLivre, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelAjouterCeLivreLayout.setVerticalGroup(
            panelAjouterCeLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAjouterCeLivreLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelAjouterCeLivre, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelAjouter.add(panelAjouterCeLivre, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, 160, -1));

        labelTitreAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelTitreAjouter.setForeground(new java.awt.Color(24, 26, 31));
        labelTitreAjouter.setText("TITRE");
        panelAjouter.add(labelTitreAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, -1, -1));

        textFieldTitreAjouter.setBackground(new java.awt.Color(240, 240, 240));
        textFieldTitreAjouter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textFieldTitreAjouter.setForeground(new java.awt.Color(24, 26, 31));
        panelAjouter.add(textFieldTitreAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 240, 40));

        labelAuteurAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelAuteurAjouter.setForeground(new java.awt.Color(24, 26, 31));
        labelAuteurAjouter.setText("AUTEUR");
        panelAjouter.add(labelAuteurAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, -1, -1));

        texteFieldAuteurAjouter.setBackground(new java.awt.Color(240, 240, 240));
        texteFieldAuteurAjouter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        texteFieldAuteurAjouter.setForeground(new java.awt.Color(24, 26, 31));
        panelAjouter.add(texteFieldAuteurAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 240, 40));

        labelNumeroAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelNumeroAjouter.setForeground(new java.awt.Color(24, 26, 31));
        labelNumeroAjouter.setText("NUMERO");
        panelAjouter.add(labelNumeroAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, -1, -1));

        textFieldNumeroAjouter.setBackground(new java.awt.Color(240, 240, 240));
        textFieldNumeroAjouter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        panelAjouter.add(textFieldNumeroAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, 240, 40));

        labelCategorieAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelCategorieAjouter.setForeground(new java.awt.Color(24, 26, 31));
        labelCategorieAjouter.setText("CATEGORIE");
        panelAjouter.add(labelCategorieAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 270, -1, -1));

        comboBoxCategorieAjouter.setBackground(new java.awt.Color(240, 240, 240));
        comboBoxCategorieAjouter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboBoxCategorieAjouter.setForeground(new java.awt.Color(24, 26, 31));
        panelAjouter.add(comboBoxCategorieAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 300, 240, 40));

        labelEmplacementAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelEmplacementAjouter.setForeground(new java.awt.Color(24, 26, 31));
        labelEmplacementAjouter.setText("EMPLACEMENT");
        panelAjouter.add(labelEmplacementAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 350, -1, -1));

        comboBoxEmplacementAjouter.setBackground(new java.awt.Color(240, 240, 240));
        comboBoxEmplacementAjouter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboBoxEmplacementAjouter.setForeground(new java.awt.Color(24, 26, 31));
        panelAjouter.add(comboBoxEmplacementAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 380, 240, 40));

        labelAjoutLogo1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelAjoutLogo1.setForeground(new java.awt.Color(197, 56, 53));
        labelAjoutLogo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAjoutLogo1.setText("AJOUT");
        panelAjouter.add(labelAjoutLogo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 180, 70));

        labelAjoutLogo2.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelAjoutLogo2.setForeground(new java.awt.Color(197, 56, 53));
        labelAjoutLogo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAjoutLogo2.setText("D'UN");
        panelAjouter.add(labelAjoutLogo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 180, 70));

        labelAjoutLogo3.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelAjoutLogo3.setForeground(new java.awt.Color(197, 56, 53));
        labelAjoutLogo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAjoutLogo3.setText("LIVRE");
        panelAjouter.add(labelAjoutLogo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 180, 70));

        panelParcourirAjouter.setBackground(new java.awt.Color(203, 65, 62));
        panelParcourirAjouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelParcourirAjouterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelParcourirAjouterMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelParcourirAjouterMousePressed(evt);
            }
        });

        labelParcourirAjouter.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelParcourirAjouter.setForeground(new java.awt.Color(233, 233, 233));
        labelParcourirAjouter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelParcourirAjouter.setText("Parcourir");

        javax.swing.GroupLayout panelParcourirAjouterLayout = new javax.swing.GroupLayout(panelParcourirAjouter);
        panelParcourirAjouter.setLayout(panelParcourirAjouterLayout);
        panelParcourirAjouterLayout.setHorizontalGroup(
            panelParcourirAjouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelParcourirAjouterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelParcourirAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelParcourirAjouterLayout.setVerticalGroup(
            panelParcourirAjouterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelParcourirAjouterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelParcourirAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelAjouter.add(panelParcourirAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 160, -1));

        labelImageAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelImageAjouter.setForeground(new java.awt.Color(24, 26, 31));
        labelImageAjouter.setText("IMAGE");
        panelAjouter.add(labelImageAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, -1, -1));

        checkBoxNumAuto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        checkBoxNumAuto.setForeground(new java.awt.Color(24, 26, 31));
        checkBoxNumAuto.setText("Activer la numérotation automatique");
        panelAjouter.add(checkBoxNumAuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 450, 230, 40));
        panelAjouter.add(labelApercuImageAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 60, 83));
        panelAjouter.add(labelCheminImageAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 433, 240, 20));

        labelAddCategorieAjouter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/add.png"))); // NOI18N
        labelAddCategorieAjouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelAddCategorieAjouterMousePressed(evt);
            }
        });
        panelAjouter.add(labelAddCategorieAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 304, -1, -1));

        labelAddEmplacementAjouter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/add.png"))); // NOI18N
        labelAddEmplacementAjouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelAddEmplacementAjouterMousePressed(evt);
            }
        });
        panelAjouter.add(labelAddEmplacementAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 384, -1, -1));

        textFieldNouveauEmplacement.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldNouveauEmplacementKeyPressed(evt);
            }
        });
        panelAjouter.add(textFieldNouveauEmplacement, new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 390, 70, -1));

        textFieldNouveauCategorie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldNouveauCategorieKeyPressed(evt);
            }
        });
        panelAjouter.add(textFieldNouveauCategorie, new org.netbeans.lib.awtextra.AbsoluteConstraints(575, 310, 70, -1));

        panelData.add(panelAjouter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelSupprimer.setPreferredSize(new java.awt.Dimension(650, 525));
        panelSupprimer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSupprimerCeLivre.setBackground(new java.awt.Color(203, 65, 62));
        panelSupprimerCeLivre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelSupprimerCeLivreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelSupprimerCeLivreMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelSupprimerCeLivreMousePressed(evt);
            }
        });

        labelSupprimerCeLivre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelSupprimerCeLivre.setForeground(new java.awt.Color(233, 233, 233));
        labelSupprimerCeLivre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSupprimerCeLivre.setText("Supprimer ce livre");

        javax.swing.GroupLayout panelSupprimerCeLivreLayout = new javax.swing.GroupLayout(panelSupprimerCeLivre);
        panelSupprimerCeLivre.setLayout(panelSupprimerCeLivreLayout);
        panelSupprimerCeLivreLayout.setHorizontalGroup(
            panelSupprimerCeLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSupprimerCeLivreLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelSupprimerCeLivre, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelSupprimerCeLivreLayout.setVerticalGroup(
            panelSupprimerCeLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSupprimerCeLivreLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelSupprimerCeLivre, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelSupprimer.add(panelSupprimerCeLivre, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, 160, -1));

        labelTitreSupprimer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelTitreSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        labelTitreSupprimer.setText("TITRE");
        panelSupprimer.add(labelTitreSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, -1, -1));

        textFieldTitreSupprimer.setEditable(false);
        textFieldTitreSupprimer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textFieldTitreSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        panelSupprimer.add(textFieldTitreSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 240, 40));

        labelAuteurSupprimer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelAuteurSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        labelAuteurSupprimer.setText("AUTEUR");
        panelSupprimer.add(labelAuteurSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, -1, -1));

        texteFieldAuteurSupprimer.setEditable(false);
        texteFieldAuteurSupprimer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        texteFieldAuteurSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        panelSupprimer.add(texteFieldAuteurSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 240, 40));

        labelNumeroSupprimer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelNumeroSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        labelNumeroSupprimer.setText("NUMERO");
        panelSupprimer.add(labelNumeroSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, -1, -1));

        textFieldNumeroSupprimer.setEditable(false);
        textFieldNumeroSupprimer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        panelSupprimer.add(textFieldNumeroSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, 240, 40));

        labelCategorieSupprimer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelCategorieSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        labelCategorieSupprimer.setText("CATEGORIE");
        panelSupprimer.add(labelCategorieSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 270, -1, -1));

        comboBoxCategorieSupprimer.setBackground(new java.awt.Color(240, 240, 240));
        comboBoxCategorieSupprimer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboBoxCategorieSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        panelSupprimer.add(comboBoxCategorieSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 300, 240, 40));

        labelEmplacementSupprimer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelEmplacementSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        labelEmplacementSupprimer.setText("EMPLACEMENT");
        panelSupprimer.add(labelEmplacementSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 350, -1, -1));

        comboBoxEmplacementSupprimer.setBackground(new java.awt.Color(240, 240, 240));
        comboBoxEmplacementSupprimer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboBoxEmplacementSupprimer.setForeground(new java.awt.Color(24, 26, 31));
        panelSupprimer.add(comboBoxEmplacementSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 380, 240, 40));

        labelsuppressionLogo1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelsuppressionLogo1.setForeground(new java.awt.Color(197, 56, 53));
        labelsuppressionLogo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelsuppressionLogo1.setText("SUPPRESSION");
        panelSupprimer.add(labelsuppressionLogo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, 70));

        labelsuppressionLogo2.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelsuppressionLogo2.setForeground(new java.awt.Color(197, 56, 53));
        labelsuppressionLogo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelsuppressionLogo2.setText("D'UN");
        panelSupprimer.add(labelsuppressionLogo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 180, 70));

        labelsuppressionLogo3.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelsuppressionLogo3.setForeground(new java.awt.Color(197, 56, 53));
        labelsuppressionLogo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelsuppressionLogo3.setText("LIVRE");
        panelSupprimer.add(labelsuppressionLogo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 180, 70));

        panelData.add(panelSupprimer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelModifier.setPreferredSize(new java.awt.Dimension(650, 525));
        panelModifier.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelModifierCeLivre.setBackground(new java.awt.Color(203, 65, 62));
        panelModifierCeLivre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelModifierCeLivreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelModifierCeLivreMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelModifierCeLivreMousePressed(evt);
            }
        });

        labelModifierCeLivre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelModifierCeLivre.setForeground(new java.awt.Color(233, 233, 233));
        labelModifierCeLivre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelModifierCeLivre.setText("Mettre à jour ce livre");

        javax.swing.GroupLayout panelModifierCeLivreLayout = new javax.swing.GroupLayout(panelModifierCeLivre);
        panelModifierCeLivre.setLayout(panelModifierCeLivreLayout);
        panelModifierCeLivreLayout.setHorizontalGroup(
            panelModifierCeLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModifierCeLivreLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelModifierCeLivre, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelModifierCeLivreLayout.setVerticalGroup(
            panelModifierCeLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelModifierCeLivreLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelModifierCeLivre, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelModifier.add(panelModifierCeLivre, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, 160, -1));

        labelTitreModifier.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelTitreModifier.setForeground(new java.awt.Color(24, 26, 31));
        labelTitreModifier.setText("TITRE");
        panelModifier.add(labelTitreModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, -1, -1));

        textFieldTitreModifier.setBackground(new java.awt.Color(240, 240, 240));
        textFieldTitreModifier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textFieldTitreModifier.setForeground(new java.awt.Color(24, 26, 31));
        panelModifier.add(textFieldTitreModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 240, 40));

        labelAuteurModifier.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelAuteurModifier.setForeground(new java.awt.Color(24, 26, 31));
        labelAuteurModifier.setText("AUTEUR");
        panelModifier.add(labelAuteurModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, -1, -1));

        texteFieldAuteurModifier.setBackground(new java.awt.Color(240, 240, 240));
        texteFieldAuteurModifier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        texteFieldAuteurModifier.setForeground(new java.awt.Color(24, 26, 31));
        panelModifier.add(texteFieldAuteurModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 240, 40));

        labelNumeroModifier.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelNumeroModifier.setForeground(new java.awt.Color(24, 26, 31));
        labelNumeroModifier.setText("NUMERO");
        panelModifier.add(labelNumeroModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, -1, -1));

        textFieldNumeroModifier.setBackground(new java.awt.Color(240, 240, 240));
        textFieldNumeroModifier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        panelModifier.add(textFieldNumeroModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, 240, 40));

        labelCategorieModifier.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelCategorieModifier.setForeground(new java.awt.Color(24, 26, 31));
        labelCategorieModifier.setText("CATEGORIE");
        panelModifier.add(labelCategorieModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 270, -1, -1));

        labelEmplacementModifier.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelEmplacementModifier.setForeground(new java.awt.Color(24, 26, 31));
        labelEmplacementModifier.setText("EMPLACEMENT");
        panelModifier.add(labelEmplacementModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 350, -1, -1));

        labelModifiactionLogo1.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelModifiactionLogo1.setForeground(new java.awt.Color(197, 56, 53));
        labelModifiactionLogo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelModifiactionLogo1.setText("MODIFICATION");
        panelModifier.add(labelModifiactionLogo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 270, 70));

        labelModifiactionLogo2.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelModifiactionLogo2.setForeground(new java.awt.Color(197, 56, 53));
        labelModifiactionLogo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelModifiactionLogo2.setText("D'UN");
        panelModifier.add(labelModifiactionLogo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 180, 70));

        labelModifiactionLogo3.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        labelModifiactionLogo3.setForeground(new java.awt.Color(197, 56, 53));
        labelModifiactionLogo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelModifiactionLogo3.setText("LIVRE");
        panelModifier.add(labelModifiactionLogo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 180, 70));

        comboBoxCategorieModifier.setBackground(new java.awt.Color(240, 240, 240));
        comboBoxCategorieModifier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboBoxCategorieModifier.setForeground(new java.awt.Color(24, 26, 31));
        panelModifier.add(comboBoxCategorieModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 300, 240, 40));

        comboBoxEmplacementModifier.setBackground(new java.awt.Color(240, 240, 240));
        comboBoxEmplacementModifier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboBoxEmplacementModifier.setForeground(new java.awt.Color(24, 26, 31));
        panelModifier.add(comboBoxEmplacementModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 380, 240, 40));

        panelParcourirModifier.setBackground(new java.awt.Color(203, 65, 62));
        panelParcourirModifier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelParcourirModifierMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelParcourirModifierMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelParcourirModifierMousePressed(evt);
            }
        });

        labelParcourirModifier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelParcourirModifier.setForeground(new java.awt.Color(233, 233, 233));
        labelParcourirModifier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelParcourirModifier.setText("Parcourir");

        javax.swing.GroupLayout panelParcourirModifierLayout = new javax.swing.GroupLayout(panelParcourirModifier);
        panelParcourirModifier.setLayout(panelParcourirModifierLayout);
        panelParcourirModifierLayout.setHorizontalGroup(
            panelParcourirModifierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelParcourirModifierLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelParcourirModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelParcourirModifierLayout.setVerticalGroup(
            panelParcourirModifierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelParcourirModifierLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelParcourirModifier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelModifier.add(panelParcourirModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, 160, -1));

        labelImageModifier.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelImageModifier.setForeground(new java.awt.Color(24, 26, 31));
        labelImageModifier.setText("IMAGE");
        panelModifier.add(labelImageModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, -1, -1));

        panelData.add(panelModifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelMain.add(panelData, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 125, -1, -1));
        panelData.getAccessibleContext().setAccessibleName("");
        panelData.getAccessibleContext().setAccessibleDescription("");

        panelButtonClose.setBackground(new java.awt.Color(24, 26, 31));
        panelButtonClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelButtonCloseMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelButtonCloseMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelButtonCloseMousePressed(evt);
            }
        });

        labelClose.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        labelClose.setForeground(new java.awt.Color(197, 56, 53));
        labelClose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClose.setText("X");

        javax.swing.GroupLayout panelButtonCloseLayout = new javax.swing.GroupLayout(panelButtonClose);
        panelButtonClose.setLayout(panelButtonCloseLayout);
        panelButtonCloseLayout.setHorizontalGroup(
            panelButtonCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelButtonCloseLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelClose, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelButtonCloseLayout.setVerticalGroup(
            panelButtonCloseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelButtonCloseLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelClose, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelMain.add(panelButtonClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(1152, 0, -1, -1));

        panelBar.setBackground(new java.awt.Color(24, 26, 31));

        javax.swing.GroupLayout panelBarLayout = new javax.swing.GroupLayout(panelBar);
        panelBar.setLayout(panelBarLayout);
        panelBarLayout.setHorizontalGroup(
            panelBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );
        panelBarLayout.setVerticalGroup(
            panelBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        panelMain.add(panelBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, -1, -1));

        panelAjouterUnLivre.setBackground(new java.awt.Color(203, 65, 62));
        panelAjouterUnLivre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelAjouterUnLivreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelAjouterUnLivreMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelAjouterUnLivreMousePressed(evt);
            }
        });

        labelAjouterUnLivre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelAjouterUnLivre.setForeground(new java.awt.Color(233, 233, 233));
        labelAjouterUnLivre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAjouterUnLivre.setText("Ajouter un livre");

        javax.swing.GroupLayout panelAjouterUnLivreLayout = new javax.swing.GroupLayout(panelAjouterUnLivre);
        panelAjouterUnLivre.setLayout(panelAjouterUnLivreLayout);
        panelAjouterUnLivreLayout.setHorizontalGroup(
            panelAjouterUnLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelAjouterUnLivre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelAjouterUnLivreLayout.setVerticalGroup(
            panelAjouterUnLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelAjouterUnLivre, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        panelMain.add(panelAjouterUnLivre, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 409, 192, -1));

        panelSupprimerUnLivre.setBackground(new java.awt.Color(203, 65, 62));
        panelSupprimerUnLivre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelSupprimerUnLivreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelSupprimerUnLivreMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelSupprimerUnLivreMousePressed(evt);
            }
        });

        labelSupprimerUnLivre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelSupprimerUnLivre.setForeground(new java.awt.Color(233, 233, 233));
        labelSupprimerUnLivre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSupprimerUnLivre.setText("Supprimer un livre");

        javax.swing.GroupLayout panelSupprimerUnLivreLayout = new javax.swing.GroupLayout(panelSupprimerUnLivre);
        panelSupprimerUnLivre.setLayout(panelSupprimerUnLivreLayout);
        panelSupprimerUnLivreLayout.setHorizontalGroup(
            panelSupprimerUnLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelSupprimerUnLivre, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
        );
        panelSupprimerUnLivreLayout.setVerticalGroup(
            panelSupprimerUnLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelSupprimerUnLivre, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        panelMain.add(panelSupprimerUnLivre, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 462, -1, -1));

        panelModifierUnLivre.setBackground(new java.awt.Color(203, 65, 62));
        panelModifierUnLivre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelModifierUnLivreMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelModifierUnLivreMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelModifierUnLivreMousePressed(evt);
            }
        });

        labelModifierUnLivre.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelModifierUnLivre.setForeground(new java.awt.Color(233, 233, 233));
        labelModifierUnLivre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelModifierUnLivre.setText("Modifier un livre");

        javax.swing.GroupLayout panelModifierUnLivreLayout = new javax.swing.GroupLayout(panelModifierUnLivre);
        panelModifierUnLivre.setLayout(panelModifierUnLivreLayout);
        panelModifierUnLivreLayout.setHorizontalGroup(
            panelModifierUnLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelModifierUnLivre, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
        );
        panelModifierUnLivreLayout.setVerticalGroup(
            panelModifierUnLivreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelModifierUnLivre, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        panelMain.add(panelModifierUnLivre, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 515, -1, -1));
        panelMain.add(labelImageCentrale, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 125, 192, 266));

        labelSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/search.png"))); // NOI18N
        labelSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                labelSearchMousePressed(evt);
            }
        });
        panelMain.add(labelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 35, -1, -1));

        textFieldSearch.setBackground(new java.awt.Color(240, 240, 240));
        textFieldSearch.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        textFieldSearch.setForeground(new java.awt.Color(24, 26, 31));
        textFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldSearchKeyPressed(evt);
            }
        });
        panelMain.add(textFieldSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 450, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void panelMainMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMainMousePressed
        this.posX = evt.getX();
        this.posY = evt.getY();
    }//GEN-LAST:event_panelMainMousePressed

    private void panelMainMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelMainMouseDragged
        int newX = evt.getXOnScreen();
        int newY = evt.getYOnScreen();
        this.setLocation(newX - this.posX, newY - this.posY);
    }//GEN-LAST:event_panelMainMouseDragged

    private void panelButtonCloseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonCloseMousePressed
        this.panelButtonCloseMousePressed();
    }//GEN-LAST:event_panelButtonCloseMousePressed

    private void panelButtonCloseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonCloseMouseExited
        this.panelButtonCloseMouseExited();
    }//GEN-LAST:event_panelButtonCloseMouseExited

    private void panelButtonCloseMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonCloseMouseEntered
        this.panelButtonCloseMouseEntered();
    }//GEN-LAST:event_panelButtonCloseMouseEntered

    private void panelButtonModifierMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonModifierMousePressed
        this.panelButtonModifierMousePressed();
    }//GEN-LAST:event_panelButtonModifierMousePressed

    private void panelButtonModifierMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonModifierMouseExited
        this.panelButtonModifierMouseExited();
    }//GEN-LAST:event_panelButtonModifierMouseExited

    private void panelButtonModifierMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonModifierMouseEntered
        this.panelButtonModifierMouseEntered();
    }//GEN-LAST:event_panelButtonModifierMouseEntered

    private void panelButtonSupprimerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonSupprimerMousePressed
        this.panelButtonSupprimerMousePressed();
    }//GEN-LAST:event_panelButtonSupprimerMousePressed

    private void panelButtonSupprimerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonSupprimerMouseExited
        this.panelButtonSupprimerMouseExited();
    }//GEN-LAST:event_panelButtonSupprimerMouseExited

    private void panelButtonSupprimerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonSupprimerMouseEntered
        this.panelButtonSupprimerMouseEntered();
    }//GEN-LAST:event_panelButtonSupprimerMouseEntered

    private void panelButtonAjouterMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonAjouterMousePressed
        this.panelButtonAjouterMousePressed();
    }//GEN-LAST:event_panelButtonAjouterMousePressed

    private void panelButtonAjouterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonAjouterMouseExited
        this.panelButtonAjouterMouseExited();
    }//GEN-LAST:event_panelButtonAjouterMouseExited

    private void panelButtonAjouterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonAjouterMouseEntered
        this.panelButtonAjouterMouseEntered();
    }//GEN-LAST:event_panelButtonAjouterMouseEntered

    private void panelButtonConsulterMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonConsulterMousePressed
        this.panelButtonConsulterMousePressed();
    }//GEN-LAST:event_panelButtonConsulterMousePressed

    private void panelButtonConsulterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonConsulterMouseExited
        this.panelButtonConsulterMouseExited();
    }//GEN-LAST:event_panelButtonConsulterMouseExited

    private void panelButtonConsulterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelButtonConsulterMouseEntered
        this.panelButtonConsulterMouseEntered();
    }//GEN-LAST:event_panelButtonConsulterMouseEntered

    private void panelAjouterUnLivreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAjouterUnLivreMousePressed
        this.panelAjouterUnLivreMousePressed();
    }//GEN-LAST:event_panelAjouterUnLivreMousePressed

    private void panelAjouterUnLivreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAjouterUnLivreMouseEntered
        this.panelAjouterUnLivreMouseEntered();
    }//GEN-LAST:event_panelAjouterUnLivreMouseEntered

    private void panelAjouterUnLivreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAjouterUnLivreMouseExited
        this.panelAjouterUnLivreMouseExited();
    }//GEN-LAST:event_panelAjouterUnLivreMouseExited

    private void panelSupprimerUnLivreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSupprimerUnLivreMousePressed
        this.panelSupprimerUnLivreMousePressed();
    }//GEN-LAST:event_panelSupprimerUnLivreMousePressed

    private void panelSupprimerUnLivreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSupprimerUnLivreMouseEntered
        this.panelSupprimerUnLivreMouseEntered();
    }//GEN-LAST:event_panelSupprimerUnLivreMouseEntered

    private void panelSupprimerUnLivreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSupprimerUnLivreMouseExited
        this.panelSupprimerUnLivreMouseExited();
    }//GEN-LAST:event_panelSupprimerUnLivreMouseExited

    private void panelModifierUnLivreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModifierUnLivreMousePressed
        this.panelModifierUnLivreMousePressed();
    }//GEN-LAST:event_panelModifierUnLivreMousePressed

    private void panelModifierUnLivreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModifierUnLivreMouseEntered
        this.panelModifierUnLivreMouseEntered();
    }//GEN-LAST:event_panelModifierUnLivreMouseEntered

    private void panelModifierUnLivreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModifierUnLivreMouseExited
        this.panelModifierUnLivreMouseExited();
    }//GEN-LAST:event_panelModifierUnLivreMouseExited

    private void panelAjouterCeLivreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAjouterCeLivreMouseEntered
        this.panelAjouterCeLivreMouseEntered();
    }//GEN-LAST:event_panelAjouterCeLivreMouseEntered

    private void panelAjouterCeLivreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAjouterCeLivreMouseExited
        this.panelAjouterCeLivreMouseExited();
    }//GEN-LAST:event_panelAjouterCeLivreMouseExited

    private void panelSupprimerCeLivreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSupprimerCeLivreMouseEntered
        this.panelSupprimerCeLivreMouseEntered();
    }//GEN-LAST:event_panelSupprimerCeLivreMouseEntered

    private void panelSupprimerCeLivreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSupprimerCeLivreMouseExited
        this.panelSupprimerCeLivreMouseExited();
    }//GEN-LAST:event_panelSupprimerCeLivreMouseExited

    private void panelSupprimerCeLivreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSupprimerCeLivreMousePressed
        this.panelSupprimerCeLivreMousePressed();
    }//GEN-LAST:event_panelSupprimerCeLivreMousePressed

    private void panelModifierCeLivreMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModifierCeLivreMouseEntered
        this.paneModifierCeLivreMouseEntered();
    }//GEN-LAST:event_panelModifierCeLivreMouseEntered

    private void panelModifierCeLivreMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModifierCeLivreMouseExited
        this.paneModifierCeLivreMouseExited();
    }//GEN-LAST:event_panelModifierCeLivreMouseExited

    private void panelModifierCeLivreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelModifierCeLivreMousePressed
        this.paneModifierCeLivreMousePressed();
    }//GEN-LAST:event_panelModifierCeLivreMousePressed

    private void panelAjouterCeLivreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAjouterCeLivreMousePressed
        this.panelAjouterCeLivreMousePressed();
    }//GEN-LAST:event_panelAjouterCeLivreMousePressed

    private void tableDatabaseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDatabaseMousePressed
        this.tableDatabaseMousePressed();
    }//GEN-LAST:event_tableDatabaseMousePressed

    private void panelParcourirAjouterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelParcourirAjouterMouseEntered
        this.panelParcourirAjouterMouseEntered();
    }//GEN-LAST:event_panelParcourirAjouterMouseEntered

    private void panelParcourirAjouterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelParcourirAjouterMouseExited
        this.panelParcourirAjouterMouseExited();
    }//GEN-LAST:event_panelParcourirAjouterMouseExited

    private void panelParcourirAjouterMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelParcourirAjouterMousePressed
        this.panelParcourirAjouterMousePressed();
    }//GEN-LAST:event_panelParcourirAjouterMousePressed

    private void panelParcourirModifierMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelParcourirModifierMouseEntered
        this.panelParcourirModifierMouseEntered();
    }//GEN-LAST:event_panelParcourirModifierMouseEntered

    private void panelParcourirModifierMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelParcourirModifierMouseExited
        this.panelParcourirModifierMouseExited();
    }//GEN-LAST:event_panelParcourirModifierMouseExited

    private void panelParcourirModifierMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelParcourirModifierMousePressed
        this.panelParcourirModifierMousePressed();
    }//GEN-LAST:event_panelParcourirModifierMousePressed

    private void labelAddCategorieAjouterMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelAddCategorieAjouterMousePressed
        this.labelAddCategorieAjouterMousePressed();
    }//GEN-LAST:event_labelAddCategorieAjouterMousePressed

    private void labelAddEmplacementAjouterMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelAddEmplacementAjouterMousePressed
        this.labelAddEmplacementAjouterMousePressed();
    }//GEN-LAST:event_labelAddEmplacementAjouterMousePressed

    private void textFieldNouveauCategorieKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldNouveauCategorieKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.textFieldNouveauCategorieKeyPressed();
        }
    }//GEN-LAST:event_textFieldNouveauCategorieKeyPressed

    private void textFieldNouveauEmplacementKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldNouveauEmplacementKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            textFieldNouveauEmplacementKeyPressed();
        }
    }//GEN-LAST:event_textFieldNouveauEmplacementKeyPressed

    private void panelAjouterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelAjouterMouseClicked
        this.panelAjouterMouseClicked();
    }//GEN-LAST:event_panelAjouterMouseClicked

    private void labelSearchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelSearchMousePressed
        this.labelSearchMousePressed();
    }//GEN-LAST:event_labelSearchMousePressed

    private void textFieldSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.textFieldSearchKeyPressed();
        }
    }//GEN-LAST:event_textFieldSearchKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Windows (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkBoxNumAuto;
    private javax.swing.JComboBox<String> comboBoxCategorieAjouter;
    private javax.swing.JComboBox<String> comboBoxCategorieModifier;
    private javax.swing.JComboBox<String> comboBoxCategorieSupprimer;
    private javax.swing.JComboBox<String> comboBoxEmplacementAjouter;
    private javax.swing.JComboBox<String> comboBoxEmplacementModifier;
    private javax.swing.JComboBox<String> comboBoxEmplacementSupprimer;
    private javax.swing.JLabel labelAddCategorieAjouter;
    private javax.swing.JLabel labelAddEmplacementAjouter;
    private javax.swing.JLabel labelAjoutLogo1;
    private javax.swing.JLabel labelAjoutLogo2;
    private javax.swing.JLabel labelAjoutLogo3;
    private javax.swing.JLabel labelAjouter;
    private javax.swing.JLabel labelAjouterCeLivre;
    private javax.swing.JLabel labelAjouterUnLivre;
    private javax.swing.JLabel labelApercuImageAjouter;
    private javax.swing.JLabel labelAuteurAjouter;
    private javax.swing.JLabel labelAuteurModifier;
    private javax.swing.JLabel labelAuteurSupprimer;
    private javax.swing.JLabel labelCategorieAjouter;
    private javax.swing.JLabel labelCategorieModifier;
    private javax.swing.JLabel labelCategorieSupprimer;
    private javax.swing.JLabel labelCheminImageAjouter;
    private javax.swing.JLabel labelClose;
    private javax.swing.JLabel labelConsulter;
    private javax.swing.JLabel labelEmplacementAjouter;
    private javax.swing.JLabel labelEmplacementModifier;
    private javax.swing.JLabel labelEmplacementSupprimer;
    private javax.swing.JLabel labelHave;
    private javax.swing.JLabel labelImageAjouter;
    private javax.swing.JLabel labelImageCentrale;
    private javax.swing.JLabel labelImageModifier;
    private javax.swing.JLabel labelModifiactionLogo1;
    private javax.swing.JLabel labelModifiactionLogo2;
    private javax.swing.JLabel labelModifiactionLogo3;
    private javax.swing.JLabel labelModifier;
    private javax.swing.JLabel labelModifierCeLivre;
    private javax.swing.JLabel labelModifierUnLivre;
    private javax.swing.JLabel labelNumeroAjouter;
    private javax.swing.JLabel labelNumeroModifier;
    private javax.swing.JLabel labelNumeroSupprimer;
    private javax.swing.JLabel labelParcourirAjouter;
    private javax.swing.JLabel labelParcourirModifier;
    private javax.swing.JLabel labelSearch;
    private javax.swing.JLabel labelSupprimer;
    private javax.swing.JLabel labelSupprimerCeLivre;
    private javax.swing.JLabel labelSupprimerUnLivre;
    private javax.swing.JLabel labelTitreAjouter;
    private javax.swing.JLabel labelTitreModifier;
    private javax.swing.JLabel labelTitreSupprimer;
    private javax.swing.JLabel labelsuppressionLogo1;
    private javax.swing.JLabel labelsuppressionLogo2;
    private javax.swing.JLabel labelsuppressionLogo3;
    private javax.swing.JPanel panelAjouter;
    private javax.swing.JPanel panelAjouterCeLivre;
    private javax.swing.JPanel panelAjouterUnLivre;
    private javax.swing.JPanel panelBar;
    private javax.swing.JPanel panelButtonAjouter;
    private javax.swing.JPanel panelButtonClose;
    private javax.swing.JPanel panelButtonConsulter;
    private javax.swing.JPanel panelButtonModifier;
    private javax.swing.JPanel panelButtonSupprimer;
    private javax.swing.JPanel panelConsulter;
    private javax.swing.JPanel panelData;
    private javax.swing.JPanel panelHave;
    private javax.swing.JPanel panelHideSeparatorAjouter;
    private javax.swing.JPanel panelHideSeparatorConsulter;
    private javax.swing.JPanel panelHideSeparatorModifier;
    private javax.swing.JPanel panelHideSeparatorSupprimer;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelModifier;
    private javax.swing.JPanel panelModifierCeLivre;
    private javax.swing.JPanel panelModifierUnLivre;
    private javax.swing.JPanel panelPane;
    private javax.swing.JPanel panelParcourirAjouter;
    private javax.swing.JPanel panelParcourirModifier;
    private javax.swing.JPanel panelSeparator;
    private javax.swing.JPanel panelSupprimer;
    private javax.swing.JPanel panelSupprimerCeLivre;
    private javax.swing.JPanel panelSupprimerUnLivre;
    private javax.swing.JScrollPane scrollpaneDatabase;
    private javax.swing.JTable tableDatabase;
    private javax.swing.JTextField textFieldNouveauCategorie;
    private javax.swing.JTextField textFieldNouveauEmplacement;
    private javax.swing.JTextField textFieldNumeroAjouter;
    private javax.swing.JTextField textFieldNumeroModifier;
    private javax.swing.JTextField textFieldNumeroSupprimer;
    private javax.swing.JTextField textFieldSearch;
    private javax.swing.JTextField textFieldTitreAjouter;
    private javax.swing.JTextField textFieldTitreModifier;
    private javax.swing.JTextField textFieldTitreSupprimer;
    private javax.swing.JTextField texteFieldAuteurAjouter;
    private javax.swing.JTextField texteFieldAuteurModifier;
    private javax.swing.JTextField texteFieldAuteurSupprimer;
    // End of variables declaration//GEN-END:variables
}
