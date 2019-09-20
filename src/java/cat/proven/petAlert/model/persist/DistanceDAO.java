package cat.proven.petAlert.model.persist;

import cat.proven.petAlert.model.Distance;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

/**
 * DAO Distance
 *
 * @author Pet Alert
 */
public class DistanceDAO {

    private final DbConnect dbConnect;

    /**
     * Constructor
     */
    public DistanceDAO() {
        dbConnect = DbConnect.getInstance();
    }

    /**
     * Search and sort alerts by given parameters.
     *
     * @param latitude param to search
     * @param longitude param to search
     * @return list of distances or null in case of error.
     */
    public List<Distance> selectWhereDistance(Double latitude, Double longitude) {
        List<Distance> found;
        String query = "{ call calcDistance(?,?) }";
        try (Connection conn = dbConnect.getConnection()) {
            if (conn != null) {
                CallableStatement st = conn.prepareCall(query);
                st.setDouble(1, latitude);
                st.setDouble(2, longitude);
                ResultSet rs = st.executeQuery();
                found = new ArrayList<>();
                while (rs.next()) {
                    Distance distances = resultSetToDistance(rs);
                    found.add(distances);
                }
            } else {
                found = null;
            }
        } catch (SQLException ex) {
            found = null;
            System.out.println("ERROR " + ex.toString());
        }

        return found;
    }

    /**
     * Gets data from current registrar of result set and convert into a
     * distance object.
     *
     * @param rs resultset to get data from
     * @return a distance with data in resultset
     */
    private Distance resultSetToDistance(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        Double distance = rs.getDouble("distance");

        Distance distances = new Distance(id, distance);

        return distances;
    }

}
