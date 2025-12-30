import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HostelService {

    public static boolean allocateRoom(String studentName) {

    try (Connection conn = DBConnection.getConnection()) {

        String roomSql =
            "SELECT room_number FROM rooms WHERE occupied < capacity LIMIT 1";
        PreparedStatement psRoom = conn.prepareStatement(roomSql);
        ResultSet rs = psRoom.executeQuery();

        if (!rs.next()) {
            return false;
        }

        int roomNumber = rs.getInt("room_number");

        String insertSql =
            "INSERT INTO students (name, room_number, status) VALUES (?, ?, 'ACTIVE')";
        PreparedStatement ps = conn.prepareStatement(insertSql);
        ps.setString(1, studentName);
        ps.setInt(2, roomNumber);
        ps.executeUpdate();

        String updateRoom =
            "UPDATE rooms SET occupied = occupied + 1 WHERE room_number = ?";
        PreparedStatement psUpdate = conn.prepareStatement(updateRoom);
        psUpdate.setInt(1, roomNumber);
        psUpdate.executeUpdate();

        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}




    // <-- FIX: return List<String[]> instead of List<String>
    public static List<String[]> getStudents() {

    List<String[]> students = new ArrayList<>();

    try (Connection conn = DBConnection.getConnection()) {

        String sql ="SELECT id, name, room_number FROM students WHERE status='ACTIVE'";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            students.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("name"),
                String.valueOf(rs.getInt("room_number"))
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return students;
}



    public static List<String[]> searchStudents(String keyword) {

    List<String[]> list = new ArrayList<>();

    try (Connection conn = DBConnection.getConnection()) {

        String sql ="SELECT id, name, room_number FROM students WHERE status='ACTIVE' AND name LIKE ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new String[]{
                String.valueOf(rs.getInt("id")),
                rs.getString("name"),
                String.valueOf(rs.getInt("room_number"))
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}



public static void markStudentLeft(int id) {

    try (Connection conn = DBConnection.getConnection()) {

        String getRoom =
            "SELECT room_number FROM students WHERE id=?";
        PreparedStatement ps1 = conn.prepareStatement(getRoom);
        ps1.setInt(1, id);
        ResultSet rs = ps1.executeQuery();
        rs.next();
        int room = rs.getInt("room_number");

        String updateStudent =
            "UPDATE students SET status='LEFT' WHERE id=?";
        PreparedStatement ps2 = conn.prepareStatement(updateStudent);
        ps2.setInt(1, id);
        ps2.executeUpdate();

        String updateRoom =
            "UPDATE rooms SET occupied = occupied - 1 WHERE room_number=?";
        PreparedStatement ps3 = conn.prepareStatement(updateRoom);
        ps3.setInt(1, room);
        ps3.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static int getTotalStudents() {

    try (Connection conn = DBConnection.getConnection()) {

        String sql = "SELECT COUNT(*) FROM students WHERE status='ACTIVE'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) return rs.getInt(1);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

public static int getAvailableRooms() {

    try (Connection conn = DBConnection.getConnection()) {

        String sql = "SELECT COUNT(*) FROM rooms WHERE occupied < capacity";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) return rs.getInt(1);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
public static int getFullRooms() {

    try (Connection conn = DBConnection.getConnection()) {

        String sql = "SELECT COUNT(*) FROM rooms WHERE occupied = capacity";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) return rs.getInt(1);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}


}
