/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resetDatabase;

import database.GestionBaseDeDonnees;

/**
 *
 * @author Pierre-Nicolas
 */
public class ResetDatabase {

    /**
     * @param args the command line argumentssssss
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GestionBaseDeDonnees gestionBaseDeDonnees = new GestionBaseDeDonnees();
        gestionBaseDeDonnees.dropTableLivreAndRecreateTableLivre();
        /*Livre livre = new Livre("", "One Piece", "Eiichirō Oda", "1", "Shonen", "1", "", null);
        gestionBaseDeDonnees.insertIntoLivre(livre);
        System.out.println(gestionBaseDeDonnees.selectAllFromLivre());
        System.out.println(gestionBaseDeDonnees.getNbColonne());*/
    }
    
}
