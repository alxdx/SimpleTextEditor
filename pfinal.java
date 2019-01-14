import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.FocusListener;
//se import asi para evitar error de ambiguedad con List de .util y .awt 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.text.DefaultEditorKit.*;
import javax.swing.text.StyledEditorKit.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;


public class pfinal{

	JFrame frame__ ;
	JTextPane editor__ ;
	UndoManager undoMgr__;
	private JComboBox<String> fontSizeComboBox__;
	private JComboBox<String> textAlignComboBox__;
	private JComboBox<String> fontFamilyComboBox__;
	private File file__;


	private static final String MAIN_TITLE = "Editor de Texto";
	private static final String DEFAULT_FONT_FAMILY = "SansSerif";
	private static final int DEFAULT_FONT_SIZE = 18;
	private static final List<String> FONT_LIST = Arrays.asList(new String [] {"Arial", "Calibri", "Cambria", "Courier New", "Comic Sans MS", "Dialog", "Georgia", "Helevetica", "Lucida Sans", "Monospaced", "Tahoma", "Times New Roman", "Verdana"});
	private static final String [] FONT_SIZES  = {"Tamanio de fuente", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30"};
	private static final String [] TEXT_ALIGNMENTS = {"Alineacion", "izquierda", "Centro", "derecha", "Justificar"};
	private static final String ELEM = AbstractDocument.ElementNameAttribute;
	private static final String COMP = StyleConstants.ComponentElementName;
	private String pictureButtonName__;

	
	JMenuBar mainMenuBar,editMenuBar;
	JMenu menu1,menu2,menu3,menu4,menu5,menu6;
	JButton boldButton,italicButton,colorButton,undoButton,redoButton;
	JMenuItem itemColor,itemNew,itemAbrir,itemGuardar,itemSalir,itemCopiar,itemPegar,itemCortar,itemImagen,itemVer,itemHelp;
	//crea la caja de texto con opciones default
	public void pfinal(){

		frame__ = new JFrame(MAIN_TITLE);

		editor__ = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor__);
		editor__.setDocument(new DefaultStyledDocument());
		undoMgr__ = new UndoManager();
		getEditorDocument().addUndoableEditListener(new UndoEditListener());

		EditItemActionListener editItemActionListener= new EditItemActionListener();
		EditButtonActionListener editButtonActionListener= new EditButtonActionListener();
		setFrameTitleWithExtn("New file");
		

		JPanel northPanel= new JPanel();
		northPanel.setLayout(new GridLayout(2,1));

		
		mainMenuBar = new JMenuBar();
		menu1 = new JMenu("Archivo");
		menu2 = new JMenu("Edicion");
		menu3 = new JMenu("Insertar");
		menu4 = new JMenu("Formato");
		menu5 = new JMenu("Ver");
		menu6 = new JMenu("Ayuda");

		mainMenuBar.add(menu1);
		mainMenuBar.add(menu2);
		mainMenuBar.add(menu3);
		mainMenuBar.add(menu4);		
		mainMenuBar.add(menu5);
		mainMenuBar.add(menu6);

		editMenuBar =new JMenuBar();

			JButton icono =new JButton();
			icono.setIcon(new ImageIcon(getClass().getResource("icons/m.jpg")));
			icono.addActionListener(new pprintListener());

		JButton undoButton = new JButton();
		undoButton.setIcon(new ImageIcon(getClass().getResource("icons/undo.png")));
		undoButton.addActionListener(new UndoActionListener("UNDO"));
		JButton redoButton = new JButton();
		redoButton.setIcon(new ImageIcon(getClass().getResource("icons/redo.png")));
		redoButton.addActionListener(new UndoActionListener("REDO"));

		boldButton=new JButton(new BoldAction());boldButton.setText("Negritas");
		boldButton.addActionListener(editButtonActionListener);
		italicButton=new JButton(new ItalicAction());italicButton.setText("Cursivas");
		italicButton.addActionListener(editButtonActionListener);

		colorButton=new JButton("Color");
		colorButton.addActionListener(new ColorActionListener());

		textAlignComboBox__ = new JComboBox<String>(TEXT_ALIGNMENTS);
		textAlignComboBox__.setEditable(false);
		textAlignComboBox__.addItemListener(new TextAlignItemListener());
		
		fontSizeComboBox__ = new JComboBox<String>(FONT_SIZES);
		fontSizeComboBox__.setEditable(false);
		fontSizeComboBox__.addItemListener(new FontSizeItemListener());

		Vector<String> editorFonts = getEditorFonts();
		editorFonts.add(0, "Tipo de Fuente");
		fontFamilyComboBox__ = new JComboBox<String>(editorFonts);
		fontFamilyComboBox__.setEditable(false);
		fontFamilyComboBox__.addItemListener(new FontFamilyItemListener());

		editMenuBar.add(undoButton);
		editMenuBar.add(redoButton);
		editMenuBar.add(boldButton);
		editMenuBar.add(italicButton);
		editMenuBar.add(colorButton);
		editMenuBar.add(textAlignComboBox__);
		editMenuBar.add(fontSizeComboBox__);
		editMenuBar.add(fontFamilyComboBox__);
		editMenuBar.add(icono);


		northPanel.add(mainMenuBar);
		northPanel.add(editMenuBar);

		//menu archivo
		itemAbrir = new JMenuItem("Abrir archivo");menu1.add(itemAbrir);
		itemAbrir.addActionListener(new OpenFileListener());
		itemGuardar = new JMenuItem("Guardar archivo");menu1.add(itemGuardar);
		itemGuardar.addActionListener(new SaveFileListener());
		itemSalir = new JMenuItem("Salir");menu1.add(itemSalir);
		itemSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		///ends menu archivo
		//menu editar
		itemNew=new JMenuItem("New");
		itemNew.addActionListener(new NewFileListener());
		menu1.add(itemNew);
		itemCortar = new JMenuItem(new CutAction());
		itemCortar.setText("cortar");
		itemCortar.addActionListener(editItemActionListener);menu2.add(itemCortar);
		itemCopiar = new JMenuItem(new CopyAction());
		itemCopiar.setText("copiar");
		itemCopiar.addActionListener(editItemActionListener);menu2.add(itemCopiar);
		itemPegar = new JMenuItem(new PasteAction());
		itemPegar.setText("pegar");
		itemPegar.addActionListener(editItemActionListener);menu2.add(itemPegar);
		
		//
		//menu insertar
		itemImagen = new JMenuItem("Isertar imagen");
		itemImagen.addActionListener(new PictureInsertActionListener());
		menu3.add(itemImagen);
		//menu formato
		itemColor = new JMenuItem("Color de fuente");
		itemColor.addActionListener(new ColorActionListener());
		menu4.add(itemColor);
		//menu ver
		itemVer = new JMenuItem("About");
		itemVer.addActionListener(new pprintListener());
		menu5.add(itemVer);
		//menu ayuda		
		itemHelp =new JMenuItem("Ayuda...");
		menu6.add(itemHelp);
		
		frame__.add(northPanel,BorderLayout.NORTH);

		frame__.add(editorScrollPane, BorderLayout.CENTER);		
		frame__.setSize(900, 500);
		frame__.setLocation(150, 80);
		frame__.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame__.setVisible(true);
		
		editor__.requestFocusInWindow();


	}


	public static void main(String[] args) {
		UIManager.put("TextPane.font",new Font(DEFAULT_FONT_FAMILY, Font.PLAIN, DEFAULT_FONT_SIZE));		
		 new pfinal().pfinal();
		
	}

	private StyledDocument getEditorDocument() {
	
		StyledDocument doc = (DefaultStyledDocument) editor__.getDocument();
		return doc;
	}
	

	private class EditItemActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			editor__.requestFocusInWindow();
		}
	}

	private class EditButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			editor__.requestFocusInWindow();
		}
	}

	private class ColorActionListener implements ActionListener {
	
		public void actionPerformed(ActionEvent e) {
		
			Color newColor = JColorChooser.showDialog(frame__, "selecciona un color",
														Color.BLACK);
			if (newColor == null) {
			
				editor__.requestFocusInWindow();
				return;
			}
			
			SimpleAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setForeground(attr, newColor);
			editor__.setCharacterAttributes(attr, false);
			editor__.requestFocusInWindow();
		}
	}

	private class UndoEditListener implements UndoableEditListener {

		@Override
		public void undoableEditHappened(UndoableEditEvent e) {

			undoMgr__.addEdit(e.getEdit()); // remember the edit
		}
	}
	private StyledDocument getNewDocument() {
	
		StyledDocument doc = new DefaultStyledDocument();
		doc.addUndoableEditListener(new UndoEditListener());
		return doc;
	}

	private class UndoActionListener implements ActionListener {
	
		private String type;
	
		public UndoActionListener(String type) {
		
			this.type = type;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (type) {
			
				case "UNDO":
					if (! undoMgr__.canUndo()) {
				
						editor__.requestFocusInWindow();
						return; // no edits to undo
					}

					undoMgr__.undo();
					break;
					
				case "REDO":
					if (! undoMgr__.canRedo()) {
				
						editor__.requestFocusInWindow();
						return; // no edits to redo
					}

					undoMgr__.redo();
			}

			editor__.requestFocusInWindow();
		}
	} 

	private class TextAlignItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			if ((e.getStateChange() != ItemEvent.SELECTED) ||
				(textAlignComboBox__.getSelectedIndex() == 0)) {
			
				return;
			}
			
			String alignmentStr = (String) e.getItem();			
			int newAlignment = textAlignComboBox__.getSelectedIndex() - 1;
			// ALIGN_LEFT 0, ALIGN_CENTER 1, ALIGN_RIGHT 2, ALIGN_JUSTIFIED 3
			textAlignComboBox__.setAction(new AlignmentAction(alignmentStr, newAlignment));	
			textAlignComboBox__.setSelectedIndex(0); //0 por default
			editor__.requestFocusInWindow();
		}
	}

	private class FontSizeItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			if ((e.getStateChange() != ItemEvent.SELECTED) ||
				(fontSizeComboBox__.getSelectedIndex() == 0)) {

				return;
			}
			
			String fontSizeStr = (String) e.getItem();			
			int newFontSize = 0;
			
			try {
				newFontSize = Integer.parseInt(fontSizeStr);
			}
			catch (NumberFormatException ex) {

				return;
			}

			fontSizeComboBox__.setAction(new FontSizeAction(fontSizeStr, newFontSize));	
			fontSizeComboBox__.setSelectedIndex(0); // initialize to (default) select
			editor__.requestFocusInWindow();
		}
	}
	private class FontFamilyItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			if ((e.getStateChange() != ItemEvent.SELECTED) ||
				(fontFamilyComboBox__.getSelectedIndex() == 0)) {
			
				return;
			}
			
			String fontFamily = (String) e.getItem();
			fontFamilyComboBox__.setAction(new FontFamilyAction(fontFamily, fontFamily));	
			fontFamilyComboBox__.setSelectedIndex(0); // initialize to (default) select
			editor__.requestFocusInWindow();
		}
	}
	private Vector<String> getEditorFonts() {
	
		String [] availableFonts =
			GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		Vector<String> returnList = new Vector<>();
	
		for (String font : availableFonts) {
	
			if (FONT_LIST.contains(font)) {

				returnList.add(font);
			}
		}
	
		return returnList;
	}
	
	private class OpenFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			file__ = chooseFile();
			
			if (file__ == null) {
			
				return;
			}
			
			readFile(file__);
			setFrameTitleWithExtn(file__.getName());
		}
		
		private File chooseFile() {
		
			JFileChooser chooser = new JFileChooser();
			
			if (chooser.showOpenDialog(frame__) == JFileChooser.APPROVE_OPTION) {
			
				return chooser.getSelectedFile();
			}
			else {
				return null;
			}
		}
		
		private void readFile(File file) {
	
			StyledDocument doc = null;
	
			try (InputStream fis = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fis)) {
			
				doc = (DefaultStyledDocument) ois.readObject();
			}
			catch (FileNotFoundException ex) {

				JOptionPane.showMessageDialog(frame__, "archivo no encontrado");
				return;
			}
			catch (ClassNotFoundException | IOException ex) {

				throw new RuntimeException(ex);
			}
			
			editor__.setDocument(doc);
			doc.addUndoableEditListener(new UndoEditListener());
			applyFocusListenerToPictures(doc);
		}
		
		private void applyFocusListenerToPictures(StyledDocument doc) {

			ElementIterator iterator = new ElementIterator(doc);
			Element element;
			
			while ((element = iterator.next()) != null) {
			
				AttributeSet attrs = element.getAttributes();
			
				if (attrs.containsAttribute(ELEM, COMP)) {

					JButton picButton = (JButton) StyleConstants.getComponent(attrs);
					picButton.addFocusListener(new PictureFocusListener());
				}
			}
		}
	}

	private class SaveFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			if (file__ == null) {
		
				file__ = chooseFile();
			
				if (file__ == null) {
			
					return;
				}
			}
			
			DefaultStyledDocument doc = (DefaultStyledDocument) getEditorDocument();
			
			try (OutputStream fos = new FileOutputStream(file__);
					ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				
				oos.writeObject(doc);
			}
			catch (IOException ex) {

				throw new RuntimeException(ex);
			}
			
			setFrameTitleWithExtn(file__.getName());
		}

		private File chooseFile() {
		
			JFileChooser chooser = new JFileChooser();
			
			if (chooser.showSaveDialog(frame__) == JFileChooser.APPROVE_OPTION) {

				return chooser.getSelectedFile();
			}
			else {
				return null;
			}
		}
	}
	private class PictureFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {

			JButton button = (JButton) e.getComponent();
			button.setBorder(new LineBorder(Color.GRAY));
			pictureButtonName__ = button.getName();
		}
		
		@Override
		public void focusLost(FocusEvent e) {

			((JButton) e.getComponent()).setBorder(new LineBorder(Color.WHITE));
		}
	}
	private class PictureInsertActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//obtiene cadena de la ubicacion del archivo
			File pictureFile = choosePictureFile();
			
			if (pictureFile == null) {
			
				editor__.requestFocusInWindow();
				return;
			}
			
			ImageIcon icon = new ImageIcon(pictureFile.toString());			
			JButton picButton = new JButton(icon);
			picButton.setBorder(new LineBorder(Color.WHITE));
			picButton.setMargin(new Insets(0,0,0,0));
			picButton.setAlignmentY(.9f);
			picButton.setAlignmentX(.9f);
			picButton.addFocusListener(new PictureFocusListener());
			picButton.setName("PICTURE_ID_" + new Random().nextInt());
			editor__.insertComponent(picButton);
			editor__.requestFocusInWindow();
		}
		
		private File choosePictureFile() {
		
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG & GIF Images", "png", "jpg", "gif");
			chooser.setFileFilter(filter);
			
			if (chooser.showOpenDialog(frame__) == JFileChooser.APPROVE_OPTION) {
			
				return chooser.getSelectedFile();
			}
			else {
				return null;
			}
		}
	}
	private void setFrameTitleWithExtn(String titleExtn) {

		frame__.setTitle(MAIN_TITLE + titleExtn);
	}
	private class NewFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			initEditorAttributes();
			editor__.setDocument(getNewDocument());
			file__ = null;
			setFrameTitleWithExtn("New file");
		}
		
		private void initEditorAttributes() {
		
			AttributeSet attrs1 = editor__.getCharacterAttributes();
			SimpleAttributeSet attrs2 = new SimpleAttributeSet(attrs1);
			attrs2.removeAttributes(attrs1);
			editor__.setCharacterAttributes(attrs2, true);
		}
	}

	private class pprintListener implements ActionListener {
	
		public void actionPerformed(ActionEvent e) {
		
			JOptionPane.showMessageDialog(null,"AUTORES: \n Martha Elena Carrera Sanchez\n Fernando Alexis Dominguez Contreras");
		}
	}
}