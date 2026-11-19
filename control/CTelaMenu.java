/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import java.awt.Dimension;
import javax.swing.JDesktopPane;
import view.TelaBolos;
import view.TelaInicial;
import view.TelaPudim;

/**
 *
 * @author alunos
 */
public class CTelaMenu {
    
    JDesktopPane jDesktop;

    public CTelaMenu (JDesktopPane jDesktop) {
        this.jDesktop = jDesktop;
    }
    
    public void abrirBolos(){
        jDesktop.removeAll();
        jDesktop.updateUI();
        Dimension resolucao = jDesktop.getSize(); 
        TelaBolos tBolo= new TelaBolos();        
        tBolo.setSize(resolucao);
        tBolo.setLocation(0, 0);
        jDesktop.add(tBolo);
        tBolo.setVisible(true);
    }
    
    public void abrirPudim(){
        jDesktop.removeAll();
        jDesktop.updateUI();
        Dimension resolucao = jDesktop.getSize(); 
        TelaPudim tPudim = new TelaPudim();        
        tPudim.setSize(resolucao);
        tPudim.setLocation(0, 0);
        jDesktop.add(tPudim);
        tPudim.setVisible(true);
    }
    

     
     
    public void abrirDescricao(){
    jDesktop.removeAll();
        jDesktop.updateUI();
        Dimension resolucao = jDesktop.getSize(); 
        TelaInicial tInicial = new TelaInicial();        
        tInicial.setSize(resolucao);
        tInicial.setLocation(0, 0);
        jDesktop.add(tInicial);
        tInicial.setVisible(true);

    
    }
}

    

