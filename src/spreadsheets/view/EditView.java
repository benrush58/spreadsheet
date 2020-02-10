package spreadsheets.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import spreadsheets.controller.Features;
import spreadsheets.model.BasicWorksheet;
import spreadsheets.model.Coord;
import spreadsheets.model.WorkbookModelView;
import spreadsheets.model.Worksheet;
import spreadsheets.model.WorksheetModelView;
import spreadsheets.model.WorksheetReader;

/**
 * Represents a view of the model in which a user can edit and interact with the data. When features
 * are added, the view is connected to a controller and a model.
 */
public class EditView extends JFrame implements WorksheetView {
  // model items to keep track of...
  private WorkbookModelView book;
  private WorksheetModelView currPage;
  private Coord selected;

  // Swing items to be added to the frame...
  private ScrollGrid grid;
  private JButton cancel;
  private JButton confirm;
  private JButton openPage;
  private JButton save;
  private JButton addCol;
  private JButton addRow;
  private JButton nameSheet;
  private JButton addPage;
  private JButton deletePage;
  private JTextField nameBox;
  private JTextField saveName;
  private JTextField editor;
  private JComboBox pageSelector;

  /**
   * Constructs an editable view attached to a WorkbookModelView, a Workbook with all of the setter
   * methods muted so as to ensure that the view cannot modify the model without the use of a
   * controller.
   */
  public EditView(WorkbookModelView book) {
    super("Microsoft Excel ^(TM)");
    this.book = book;
    this.currPage = (WorksheetModelView) book.getCurrentPage();

    // sets the preferred size and layout of the frame
    this.setPreferredSize(new Dimension(800, 600));
    this.setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(true);

    // initialize the selected cell to (1, 1), a default cell
    this.selected = new Coord(1, 1);

    // creates the components to be added to the frame
    this.grid = new ScrollGrid(this.currPage);
    this.cancel = new JButton("Cancel");
    this.confirm = new JButton("Confirm");
    this.addCol = new JButton("Add Columns");
    this.addRow = new JButton("Add Rows");
    this.openPage = new JButton("Open Sheet");
    this.save = new JButton("Save as:");
    this.saveName = new JTextField(20);
    this.editor = new JTextField(this.currPage.getCell(selected).getRawData(), 40);

    // adds components to the top panel
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout());
    topPanel.add(this.cancel);
    topPanel.add(this.confirm);
    topPanel.add(this.editor);

    // adds components to the bottom panel
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout());
    bottomPanel.add(this.addCol);
    bottomPanel.add(this.addRow);
    bottomPanel.add(this.save);
    bottomPanel.add(this.saveName);

    // adds a panel on the right side of the frame for Workbook functionality
    JPanel sidePanel = new JPanel();
    sidePanel.setPreferredSize(new Dimension(200, 500));
    sidePanel.setLayout(new FlowLayout());

    // creates the necessary elements for the side panel
    this.addPage = new JButton("New Sheet");
    this.nameSheet = new JButton("Confirm");
    this.deletePage = new JButton("Delete Page");
    this.nameBox = new JTextField(15);
    String[] pageNames = this.book.getPages();
    this.pageSelector = new JComboBox<>(pageNames);

    // adds the elements to the side panel
    sidePanel.add(new JLabel("                                                   "));
    sidePanel.add(this.openPage);
    sidePanel.add(this.addPage);
    sidePanel.add(new JLabel("                                                   "));
    sidePanel.add(new JLabel("Rename the sheet:"));
    sidePanel.add(this.nameBox);
    sidePanel.add(this.nameSheet);
    sidePanel.add(new JLabel("                                                   "));
    sidePanel.add(new JLabel("Which sheet are you working on?"));
    sidePanel.add(pageSelector);
    sidePanel.add(new JLabel("                                                   "));
    sidePanel.add(new JLabel("                                                   "));
    sidePanel.add(new JLabel("                                                   "));
    sidePanel.add(deletePage);

    // adds all of the panels to the frame
    this.add(topPanel, BorderLayout.PAGE_START);
    this.add(this.grid, BorderLayout.CENTER);
    this.add(bottomPanel, BorderLayout.PAGE_END);
    this.add(sidePanel, BorderLayout.EAST);

    // pack the frame and make it visible
    this.setFocusable(true);
    this.setAutoRequestFocus(true);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void addFeatures(Features f) {

    // clears the text field when pressed
    cancel.addActionListener(evt -> {
      this.editor.setText("");
      resetFocus();
    });

    // calls for the controller to set a cells contents when pressed
    confirm.addActionListener(evt -> {
      f.setCell(currPage.getName(), selected, this.editor.getText());
      resetFocus();
    });

    // adds columns to the view when pressed
    addCol.addActionListener(e -> {
      this.addColumns();
      resetFocus();
    });

    // adds rows to the view when pressed
    addRow.addActionListener(evt -> {
      this.addRows();
      resetFocus();
    });

    // renames the current sheet
    nameSheet.addActionListener(evt -> {
      if (nameBox.getText().length() > 0) {
        String currName = currPage.getName();
        String newName = nameBox.getText();

        f.renamePage(currName, newName);

        pageSelector.addItem(newName);
        pageSelector.removeItem(currName);

        nameBox.setText("");

        setPage();
        resetFocus();
      }
    });

    // attempts to load in a file when pressed
    openPage.addActionListener(evt -> {
      try {
        this.openFile(f);
      } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(this, "File could not be found!");
      }
      resetFocus();
    });

    // attempts to add a blank page to the Workbook, but if there's already a sheet with
    // that name, the user is informed and nothing happens
    addPage.addActionListener(evt -> {
      String name = JOptionPane.showInputDialog(this, "Please enter a name for new page!");
      try {
        f.addBlankPage(name);
        pageSelector.addItem(name);
      } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, "Sorry, there is already a sheet with that name!");
      }
      resetFocus();
    });

    // connects the page selector with appropriate pages in the model
    pageSelector.addActionListener(evt -> {
      String selected = (String) pageSelector.getSelectedItem();
      f.setCurrentPage(selected);
      editor.setText("");
      resetFocus();
    });

    deletePage.addActionListener(evt -> {
      if (book.getPages().length <= 1) {
        int i = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?");
        if (i == 0) {
          System.exit(1);
        }
      } else {
        int i = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete page?");
        if (i == 0) {
          String page = currPage.getName();
          pageSelector.removeItem(page);
          f.removePage(page);
          currPage = new WorksheetModelView(book.getPage(book.getPages()[0]));
        }
      }
    });

    // attempts to save the current sheet to the file name specified
    save.addActionListener(evt -> {
      try {
        f.savePage(currPage.getName(), this.saveName.getText());
        JOptionPane.showMessageDialog(this, "File successfully saved.");
      } catch (FileNotFoundException e) {
        JOptionPane.showMessageDialog(this, "File could not be saved!");
      }
      resetFocus();
    });

    // adds listener to the mouse
    this.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        // if the click is within the correct cell range,
        // select the appropriate cell
        if (e.getX() > 50 && e.getY() > 90) {
          Coord coord = findClick(e.getX(), e.getY() - 60);
          f.selectCell(currPage.getName(), coord);
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // not used in this implementation
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // not used in this implementation
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // not used in this implementation
      }

      @Override
      public void mouseExited(MouseEvent e) {
        // not used in this implementation
      }
    });

    // adds listener to any keys pressed
    this.addKeyListener(new KeyListener() {
      //This adds all the key listeners for whatever keys we decide to implement
      @Override
      public void keyTyped(KeyEvent e) {
        // not used in this implementation
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // the key that was typed and currently selected cell
        int key = e.getKeyCode();
        Coord curr = grid.getSelected();

        // move the selection depending on the key
        switch (key) {
          case KeyEvent.VK_UP:
            if (curr.row > 1) {
              highlightCell(new Coord(curr.col, curr.row - 1));
            }
            break;
          case KeyEvent.VK_DOWN:
            if (curr.row < grid.getMaxRow()) {
              highlightCell(new Coord(curr.col, curr.row + 1));
            }
            break;
          case KeyEvent.VK_LEFT:
            if (curr.col > 1) {
              highlightCell(new Coord(curr.col - 1, curr.row));
            }
            break;
          case KeyEvent.VK_RIGHT:
            if (curr.row < grid.getMaxCol()) {
              highlightCell(new Coord(curr.col + 1, curr.row));
            }
            break;
          case KeyEvent.VK_DELETE:
            f.deleteCell(currPage.getName(), selected);
            editor.setText("");
            break;
          default:
        }
        resetFocus();
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // not used in this implementation
      }
    });
  }

  @Override
  public void setPage() {
    this.currPage = (WorksheetModelView) this.book.getCurrentPage();
    this.grid.loadModel(this.currPage);
  }

  // adds extra empty rows to the grid
  private void addRows() {
    this.grid.addRows();
  }

  // adds extra empty columns to the grid
  private void addColumns() {
    this.grid.addColumns();
  }

  @Override
  public void highlightCell(Coord coord) {
    this.grid.setSelected(coord);
    this.selected = grid.getSelected();
    this.editor.setText(this.currPage.getCell(this.selected).getRawData());
  }

  // finds the coordinates of the cell that was clicked
  // on based on the raw data from the click
  private Coord findClick(int x, int y) {
    return this.grid.findClick(x, y);
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  // Attempts to open a file from the User's computer and load it into the MVC architecture.
  // Must be a text file formatted with correct syntax for being read in.
  private void openFile(Features f) throws FileNotFoundException {
    // creates a file chooser
    JFileChooser fileChooser = new JFileChooser();

    // filters out all files that are not text files
    fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

    // this represents the choice that the user has made
    int choice = fileChooser.showOpenDialog(getParent());
    // if a file was selected...
    if (choice == JFileChooser.APPROVE_OPTION) {
      // gets the file from the file chooser and creates a new FileReader
      FileReader file = new FileReader(fileChooser.getSelectedFile());

      // creates a WorksheetBuilder
      WorksheetReader.WorksheetBuilder<Worksheet> builder
              = new BasicWorksheet.BasicWorksheetBuilder();

      // creates a Worksheet using the builder and the file reader
      Worksheet ws = WorksheetReader.read(builder, file);

      // clears the text editors
      this.editor.setText("");
      this.saveName.setText("");

      // calls for the controller to load the model, which will update the
      // model in both the controller and the view
      f.loadPage(fileChooser.getSelectedFile().getName(), ws);
      this.currPage = new WorksheetModelView(ws);
      this.pageSelector.addItem(fileChooser.getSelectedFile().getName());
    }
  }



}

