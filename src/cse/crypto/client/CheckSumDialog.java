package cse.crypto.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import cse.crypto.encryption.HashGenerationException;
import cse.crypto.encryption.HashGeneratorUtils;
import cse.crypto.helper.Debug;

public class CheckSumDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JFileChooser fileChooserSrc;
	protected JFileChooser fileChooserDes;
	private JTextField txtSrc;
	private JTextField txtDes;
	
	JLabel lbMd5;
	JLabel lbSrcMd5;
	JLabel lbDestMd5;
	JLabel lbMd5Comp;
	
	JLabel lbSha1;
	JLabel lblSrcSha1;
	JLabel lbDestSha1;
	JLabel lbSha1Comp;
	
	private JLabel lbSha256;
	private JLabel lbSha256Src;
	private JLabel lbSha256Dest;
	private JLabel lbSha256Comp;
	
	public CheckSumDialog() {
		setTitle("Checksum");
		setSize(612, 391);
		
		fileChooserSrc = new JFileChooser();
		fileChooserSrc.setDialogTitle("Choose a Src file");
		
		fileChooserDes = new JFileChooser();
		fileChooserDes.setDialogTitle("Choose a Dest File");
		
		
		JButton btnSourceFile = new JButton("Src file...");
		btnSourceFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int retBrowse = fileChooserSrc.showDialog(rootPane, "Browse File");
				if(retBrowse == JFileChooser.APPROVE_OPTION){
					txtSrc.setText(fileChooserSrc.getSelectedFile().getAbsolutePath());
					setMd5LabelVisible(false);
					setSha1LabelVisible(false);
					setSha256LabelVisible(false);
				}
			}
		});
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		getContentPane().add(btnSourceFile);
		
		JButton btnDestination = new JButton("Dest file...");
		springLayout.putConstraint(SpringLayout.WEST, btnDestination, 0, SpringLayout.WEST, btnSourceFile);
		btnDestination.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int retBrowse = fileChooserDes.showDialog(rootPane, "Browse File");
				if(retBrowse == JFileChooser.APPROVE_OPTION){
					txtDes.setText(fileChooserDes.getSelectedFile().getAbsolutePath());
					setMd5LabelVisible(false);
					setSha1LabelVisible(false);
					setSha256LabelVisible(false);
				}
			}
		});
		
		JButton btnCheckSha = new JButton("Check SUM");
		btnCheckSha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCheckSum();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnCheckSha, 42, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnCheckSha, -115, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnCheckSha);
		getContentPane().add(btnDestination);
		
		txtSrc = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, btnSourceFile, -1, SpringLayout.NORTH, txtSrc);
		springLayout.putConstraint(SpringLayout.WEST, btnSourceFile, 5, SpringLayout.EAST, txtSrc);
		springLayout.putConstraint(SpringLayout.NORTH, txtSrc, 24, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, txtSrc, 9, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtSrc, -106, SpringLayout.WEST, btnCheckSha);
		getContentPane().add(txtSrc);
		txtSrc.setColumns(10);
		txtSrc.setEditable(false);
		
		txtDes = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, txtDes, 9, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtDes, -6, SpringLayout.WEST, btnDestination);
		springLayout.putConstraint(SpringLayout.NORTH, btnDestination, -1, SpringLayout.NORTH, txtDes);
		springLayout.putConstraint(SpringLayout.NORTH, txtDes, 12, SpringLayout.SOUTH, txtSrc);
		getContentPane().add(txtDes);
		txtDes.setColumns(10);
		txtDes.setEditable(false);
		
		lbMd5 = new JLabel("MD5:");
		springLayout.putConstraint(SpringLayout.WEST, lbMd5, 0, SpringLayout.WEST, txtSrc);
		getContentPane().add(lbMd5);
		
		JLabel label = new JLabel("----------------------------------------------------------------------------------------------------------------------------------------------");
		springLayout.putConstraint(SpringLayout.NORTH, lbMd5, 6, SpringLayout.SOUTH, label);
		springLayout.putConstraint(SpringLayout.NORTH, label, 6, SpringLayout.SOUTH, btnDestination);
		springLayout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, txtSrc);
		springLayout.putConstraint(SpringLayout.EAST, label, -11, SpringLayout.EAST, getContentPane());
		getContentPane().add(label);
		
		lbSrcMd5 = new JLabel("Src File:");
		springLayout.putConstraint(SpringLayout.NORTH, lbSrcMd5, 6, SpringLayout.SOUTH, lbMd5);
		springLayout.putConstraint(SpringLayout.WEST, lbSrcMd5, 34, SpringLayout.WEST, getContentPane());
		getContentPane().add(lbSrcMd5);
		
		lbDestMd5 = new JLabel("Des File:");
		springLayout.putConstraint(SpringLayout.NORTH, lbDestMd5, 6, SpringLayout.SOUTH, lbSrcMd5);
		springLayout.putConstraint(SpringLayout.WEST, lbDestMd5, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lbDestMd5);
		
		lbMd5Comp = new JLabel("Two files have the same MD5");
		springLayout.putConstraint(SpringLayout.NORTH, lbMd5Comp, 6, SpringLayout.SOUTH, lbDestMd5);
		springLayout.putConstraint(SpringLayout.WEST, lbMd5Comp, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lbMd5Comp);
		
		lbSha1 = new JLabel("SHA-1:");
		springLayout.putConstraint(SpringLayout.NORTH, lbSha1, 6, SpringLayout.SOUTH, lbMd5Comp);
		springLayout.putConstraint(SpringLayout.WEST, lbSha1, 0, SpringLayout.WEST, txtSrc);
		getContentPane().add(lbSha1);
		
		lblSrcSha1 = new JLabel("Src File:");
		springLayout.putConstraint(SpringLayout.NORTH, lblSrcSha1, 6, SpringLayout.SOUTH, lbSha1);
		springLayout.putConstraint(SpringLayout.WEST, lblSrcSha1, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lblSrcSha1);
		
		lbDestSha1 = new JLabel("Des File:");
		springLayout.putConstraint(SpringLayout.NORTH, lbDestSha1, 6, SpringLayout.SOUTH, lblSrcSha1);
		springLayout.putConstraint(SpringLayout.WEST, lbDestSha1, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lbDestSha1);
		
		lbSha1Comp = new JLabel("Two files have the same SHA-1");
		springLayout.putConstraint(SpringLayout.NORTH, lbSha1Comp, 2, SpringLayout.SOUTH, lbDestSha1);
		springLayout.putConstraint(SpringLayout.WEST, lbSha1Comp, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lbSha1Comp);
		
		lbSha256 = new JLabel("SHA-256:");
		springLayout.putConstraint(SpringLayout.NORTH, lbSha256, 6, SpringLayout.SOUTH, lbSha1Comp);
		springLayout.putConstraint(SpringLayout.WEST, lbSha256, 0, SpringLayout.WEST, txtSrc);
		getContentPane().add(lbSha256);
		
		lbSha256Src = new JLabel(" Src file:");
		springLayout.putConstraint(SpringLayout.NORTH, lbSha256Src, 6, SpringLayout.SOUTH, lbSha256);
		springLayout.putConstraint(SpringLayout.WEST, lbSha256Src, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lbSha256Src);
		
		lbSha256Dest = new JLabel("Dest File:");
		springLayout.putConstraint(SpringLayout.NORTH, lbSha256Dest, 6, SpringLayout.SOUTH, lbSha256Src);
		springLayout.putConstraint(SpringLayout.WEST, lbSha256Dest, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lbSha256Dest);
		
		lbSha256Comp = new JLabel("Two file have the same SHA-256");
		springLayout.putConstraint(SpringLayout.NORTH, lbSha256Comp, 6, SpringLayout.SOUTH, lbSha256Dest);
		springLayout.putConstraint(SpringLayout.WEST, lbSha256Comp, 0, SpringLayout.WEST, lbSrcMd5);
		getContentPane().add(lbSha256Comp);
		
		setMd5LabelVisible(false);
		setSha1LabelVisible(false);
		setSha256LabelVisible(false);
	}
	
	
	private void setMd5LabelVisible(boolean isVisible){
		lbMd5.setVisible(isVisible);
		lbSrcMd5.setVisible(isVisible);
		lbDestMd5.setVisible(isVisible);
		lbMd5Comp.setVisible(isVisible);
	}
	
	private void setSha1LabelVisible(boolean isVisible){
		lbSha1.setVisible(isVisible);
		lblSrcSha1.setVisible(isVisible);
		lbDestSha1.setVisible(isVisible);
		lbSha1Comp.setVisible(isVisible);
	}
	
	private void setSha256LabelVisible(boolean isVisible){
		lbSha256.setVisible(isVisible);
		lbSha256Src.setVisible(isVisible);
		lbSha256Dest.setVisible(isVisible);
		lbSha256Comp.setVisible(isVisible);
	}
	
	
	private void doCheckSum(){
		File src = fileChooserSrc.getSelectedFile();
		File dest = fileChooserDes.getSelectedFile();
		if(src == null || !src.exists() || dest == null || !dest.exists()){
			Debug.d("doCheckSum()", "Dest Or Src File Not Found");
			JOptionPane.showMessageDialog(this, "Dest Or Src File Not Found");
			return;
		}
		//CHECK MD5
		try {
			String srcMd5 = HashGeneratorUtils.generateMD5(src);
			String destMd5 = HashGeneratorUtils.generateMD5(dest);

			lbSrcMd5.setText("Src file: " + srcMd5);
			lbDestMd5.setText("Dest file: " +destMd5);
			lbMd5Comp.setText(srcMd5.equals(destMd5) ? 
					"Two files have the same MD5"
					:"Two files don't have the same MD5"
					);
			
			setMd5LabelVisible(true);
			
		} catch (HashGenerationException e) {
			e.printStackTrace();
		}
		
		//CHECK SHA-1
		try{
			String srcSHA1 = HashGeneratorUtils.generateSHA1(src);
			String destSha1 = HashGeneratorUtils.generateSHA1(dest);
			lblSrcSha1.setText("Src file: " + srcSHA1);
			lbDestSha1.setText("Des file: " +destSha1);
			lbSha1Comp.setText(srcSHA1.equals(destSha1) ? 
					"Two files have the same SHA-1"
					:"Two files don't have the same SHA-1"
					);
			setSha1LabelVisible(true);	
			
		} catch (HashGenerationException e) {
			e.printStackTrace();
		}
		//CHECK SHA-256
		try{		
			String sha256Src = HashGeneratorUtils.generateSHA256(src);
			String sha256Dest = HashGeneratorUtils.generateSHA256(dest);
			lbSha256Src.setText("Src file: " + sha256Src);
			lbSha256Dest.setText("Dest file: " + sha256Dest);
			lbSha256Comp.setText(sha256Src.equals(sha256Dest) ? 
					"Two files have the same SHA-256"
					:"Two files don't have the same SHA-256"
					);
		 setSha256LabelVisible(true);
		} catch (HashGenerationException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
