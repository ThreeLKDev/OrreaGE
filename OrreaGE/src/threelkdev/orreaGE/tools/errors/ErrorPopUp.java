package threelkdev.orreaGE.tools.errors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class ErrorPopUp {

	private static final int PAD = 8;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 490;
	
	public static void showPopUp( String title, String message, String errorMessage ) {
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		JFrame frame = createFrame( title );
		addMessage( message, frame );
		JTextArea textField = createTextArea( errorMessage );
		addScrollPanel( textField, frame );
		frame.setVisible( true );
	}
	
	private static JFrame createFrame( String title ) {
		JFrame frame = new JFrame();
		frame.setResizable( false );
		frame.setTitle( title );
		frame.setSize( WIDTH, HEIGHT );
		frame.setLocationRelativeTo( null );
		frame.setLayout( new BorderLayout() );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		return frame;
	}
	
	private static void addMessage( String message, JFrame frame ) {
		JTextArea label = new JTextArea( message );
		label.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 12 ) );
		label.setEditable( false );
		label.setLineWrap( true );
		label.setWrapStyleWord( true );
		label.setMargin( new Insets( PAD, PAD, PAD, PAD ) );
		frame.add( label, BorderLayout.NORTH );
	}
	
	private static JTextArea createTextArea( String errorMessage ) {
		JTextArea field = new JTextArea( errorMessage );
		field.setMargin( new Insets( PAD, PAD, PAD, PAD ) );
		field.setForeground( Color.RED );
		field.setEditable( false );
		field.setLineWrap( true );
		field.setWrapStyleWord( true );
		return field;
	}
	
	private static void addScrollPanel( JTextArea textField, JFrame frame ) {
		JScrollPane scrollPane = new JScrollPane( textField );
		scrollPane.getInsets( new Insets( PAD, PAD, PAD, PAD ) );
		scrollPane.setBorder( new EmptyBorder( PAD, PAD, PAD, PAD ) );
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		frame.add( scrollPane, BorderLayout.CENTER );
	}
	
}
