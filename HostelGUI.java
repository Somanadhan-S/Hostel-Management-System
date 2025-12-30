import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class HostelGUI extends JFrame {

    private JTextField nameField;
    private JButton allocateButton, viewButton;
    private JTable studentTable;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel totalLabel, availableLabel, fullLabel;

    public HostelGUI() {
        setTitle("Hostel Room Allocation");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        totalLabel = new JLabel();
        availableLabel = new JLabel();
        fullLabel = new JLabel();

        add(totalLabel);
        add(availableLabel);
        add(fullLabel);

        updateDashboard();

        JLabel nameLabel = new JLabel("Student Name:");
        nameField = new JTextField(20);
        allocateButton = new JButton("Allocate Room");
        viewButton = new JButton("View Students");
        JButton deleteButton = new JButton("Delete Student");
         JLabel searchLabel = new JLabel("Search Name:");
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        add(nameLabel);
        add(nameField);
        add(allocateButton);
        add(viewButton);
        add(deleteButton);
        add(searchLabel);
        add(searchField);
        add(searchButton);

        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        add(scrollPane);

        // Allocate Room button
        allocateButton.addActionListener(e -> {

    String name = nameField.getText().trim();

    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter student name");
        return;
    }

    boolean success = HostelService.allocateRoom(name);

    if (success) {
        JOptionPane.showMessageDialog(this, "Room allocated to " + name);
        nameField.setText("");
        loadStudents();
        updateDashboard();
    } else {
        JOptionPane.showMessageDialog(this, "No rooms available!");
    }
});

        // View Students button
        viewButton.addActionListener(e -> loadStudents());

        searchButton.addActionListener(e -> {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
        loadStudents(); // show all
        } else {
        loadSearchResults(keyword);
    }
});




   deleteButton.addActionListener(e -> {

    int row = studentTable.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a student to delete");
        return;
    }

    int id = Integer.parseInt(
        studentTable.getValueAt(row, 0).toString()
    );

    HostelService.markStudentLeft(id);

    loadStudents();
    updateDashboard();
});


    }

    private void loadStudents() {

    List<String[]> students = HostelService.getStudents();

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID");
    model.addColumn("Student Name");
    model.addColumn("Room Number");

    for (String[] s : students) {
        model.addRow(new Object[]{ s[0], s[1], s[2] });
    }

    studentTable.setModel(model);
}

private void updateDashboard() {

    totalLabel.setText(
        "Total Students: " + HostelService.getTotalStudents());

    availableLabel.setText(
        "Available Rooms: " + HostelService.getAvailableRooms());

    fullLabel.setText(
        "Full Rooms: " + HostelService.getFullRooms());
}

private void loadSearchResults(String keyword) {

    List<String[]> students = HostelService.searchStudents(keyword);

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID");
    model.addColumn("Student Name");
    model.addColumn("Room Number");

    for (String[] s : students) {
        model.addRow(new Object[]{ s[0], s[1], s[2] });
    }

    studentTable.setModel(model);
}


}
