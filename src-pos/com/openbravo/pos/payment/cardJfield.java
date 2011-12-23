//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.payment;

import com.openbravo.pos.forms.AppLocal;
import javax.swing.*;
import posbravo.com.db.MyJDBC;

public class cardJfield extends javax.swing.JPanel implements PaymentPanel {
    
    private JPaymentNotifier m_notifier;
    private MagCardReader m_cardreader;
    private String track1 = null;
    private String track2 = null;
    private String track3 = null;
    private String m_sTransactionID;
    private double m_dTotal;
    
    /** Creates new form JMagCardReader */
    // public PaymentPanelMagCard(String sReader, JPaymentNotifier notifier) {
    public cardJfield(MagCardReader cardreader, JPaymentNotifier notifier) {
        
        m_notifier = notifier;
        m_cardreader = cardreader;

        initComponents();
        
        if (m_cardreader != null) {
            // Se van a poder efectuar pagos con tarjeta
            m_jKeyFactory.addKeyListener(new KeyBarsListener());   
            jReset.setEnabled(true);
        } else {
            jReset.setEnabled(false);
        }
    }
    
    public JComponent getComponent(){
        return this;
    }
    
    public void activate(String sTransaction, double dTotal) {
        
        m_sTransactionID = sTransaction;
        m_dTotal = dTotal;
        
        resetState();
        
        m_jKeyFactory.setText(null);       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_jKeyFactory.requestFocus();
            }
        });
    }
    
    private void resetState() {
        
        m_notifier.setStatus(false, false);  
              
        nameJField.setText(null);
        cardJField.setText(null);
        expJField.setText(null);
        track1 = null;
        track2 = null;
        track3 = null;
        
        if (m_cardreader != null) {
            // Se van a poder efectuar pagos con tarjeta
            m_cardreader.getMagCard().reset();
        }
    }
    
    public PaymentInfoMagcard getPaymentInfoMagcard() {
		MyJDBC jd = new MyJDBC();
		jd.getConnection();
		jd.insertCCInfo(m_sTransactionID,track1,track2,track3,nameJField.getText(),cardJField.getText(),expJField.getText(),m_dTotal,PaymentInfoMagcard.getCardType(cardJField.getText()),null,null);

        if (m_dTotal > 0.0) {
            return new PaymentInfoMagcard(
                    nameJField.getText(),
                    cardJField.getText(), 
                    expJField.getText(),
                    track1,
                    track2,
                    track3,
                    m_sTransactionID,
                    m_dTotal);
        } else {
            return new PaymentInfoMagcardRefund(
                    nameJField.getText(),
                    cardJField.getText(), 
                    expJField.getText(),
                    track1,
                    track2,
                    track3,
                    m_sTransactionID,
                    m_dTotal);
        }
    } 
    
    private void stateTransition() {
        
        if (m_cardreader.getMagCard().isComplete()) {
            nameJField.setText(m_cardreader.getMagCard().getHolderName());
            cardJField.setText(m_cardreader.getMagCard().getCardNumber());
            expJField.setText(m_cardreader.getMagCard().getExpirationDate());
            track1 = m_cardreader.getMagCard().getTrack1();
            track2 = m_cardreader.getMagCard().getTrack2();
            track3 = m_cardreader.getMagCard().getTrack3();
            m_notifier.setStatus(true, true);  
        } else {
            nameJField.setText(null);
            cardJField.setText(null);
            expJField.setText(null); 
            track1 = null;
            track3 = null;
            track3 = null;
            m_notifier.setStatus(false, false);  
        }      
    }    
    
    private class KeyBarsListener extends java.awt.event.KeyAdapter {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            m_cardreader.keyPressed(evt);
            stateTransition();
        }
        public void keyReleased(java.awt.event.KeyEvent evt) {
            m_cardreader.keyReleased(evt);
            stateTransition();
        }
        public void keyTyped(java.awt.event.KeyEvent evt){
            m_jKeyFactory.setText(null);
            m_cardreader.keyTyped(evt);
            stateTransition(); // e.getKeyChar()
        }
    }   
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jReset = new javax.swing.JButton();
        m_jKeyFactory = new javax.swing.JTextArea();
        expJField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        nameJField = new javax.swing.JTextField();
        cardJField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setText(AppLocal.getIntString("message.paymentgatewayswipe")); // NOI18N
        jPanel2.add(jLabel1);

        add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(null);

        jReset.setText(AppLocal.getIntString("button.reset")); // NOI18N
        jReset.setFocusPainted(false);
        jReset.setFocusable(false);
        jReset.setRequestFocusEnabled(false);
        jReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jResetActionPerformed(evt);
            }
        });
        jPanel1.add(jReset);
        jReset.setBounds(380, 20, 90, 30);
        jPanel1.add(m_jKeyFactory);
        m_jKeyFactory.setBounds(0, 0, 0, 0);

        expJField.setText("Enter Exp. Date");
        jPanel1.add(expJField);
        expJField.setBounds(110, 90, 100, 30);

        jLabel9.setText(AppLocal.getIntString("label.cardholder")); // NOI18N
        jPanel1.add(jLabel9);
        jLabel9.setBounds(20, 20, 100, 14);

        jLabel10.setText(AppLocal.getIntString("label.cardnumber")); // NOI18N
        jPanel1.add(jLabel10);
        jLabel10.setBounds(20, 60, 100, 14);

        jLabel11.setText(AppLocal.getIntString("label.cardexpdate")); // NOI18N
        jPanel1.add(jLabel11);
        jLabel11.setBounds(20, 100, 100, 14);

        nameJField.setText("Enter Holder Name");
        nameJField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameJFieldActionPerformed(evt);
            }
        });
        jPanel1.add(nameJField);
        nameJField.setBounds(110, 10, 250, 30);

        cardJField.setText("Enter Card Number");
        jPanel1.add(cardJField);
        cardJField.setBounds(110, 50, 250, 30);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jResetActionPerformed

        resetState();
        
    }//GEN-LAST:event_jResetActionPerformed

private void nameJFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameJFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_nameJFieldActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cardJField;
    private javax.swing.JTextField expJField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jReset;
    private javax.swing.JTextArea m_jKeyFactory;
    private javax.swing.JTextField nameJField;
    // End of variables declaration//GEN-END:variables
    
}
