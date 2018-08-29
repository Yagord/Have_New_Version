/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.image.BufferedImage;

/**
 *
 * @author Pierre-Nicolas
 */
public class Livre {
    private String id;
    private String titre;
    private String auteur;
    private String numero;
    private String categorie;
    private String emplacement;
    private String cheminImage;
    private BufferedImage image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    
    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        this.cheminImage = cheminImage;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public Livre(String id, String titre, String auteur, String numero, String categorie, String emplacement, String chemin, BufferedImage image) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.numero = numero;
        this.categorie = categorie;
        this.emplacement = emplacement;
        this.cheminImage = chemin;
        this.image = image;
    }
    
    @Override
    public String toString() {
        return this.id + ", " + this.titre + ", " + this.auteur + ", " + this.numero + ", " + this.categorie + ", " + this.emplacement;
    }
    
}
