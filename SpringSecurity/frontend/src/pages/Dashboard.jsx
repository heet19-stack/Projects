import { useEffect, useState } from "react";
import API from "../services/api";

function Dashboard() {

  const [requests, setRequests] = useState([]);

  useEffect(() => {

    fetchRequests();

  }, []);

  const fetchRequests = async () => {

    try {

      const response = await API.get("/signup/allRequest");

      setRequests(response.data);

    } catch (error) {

      console.log(error);
    }
  };

  const approveRequest = async (id) => {

    try {

      await API.put(`/admin/${id}`);

      alert("Request Approved");

      fetchRequests();

    } catch (error) {

      alert("Approval Failed");
    }
  };

  return (
    <div style={{ padding: "30px" }}>

      <h1>Admin Dashboard</h1>

      {
        requests.map((req) => (

          <div
            key={req.id}
            style={{
              border: "1px solid black",
              marginBottom: "10px",
              padding: "10px"
            }}
          >

            <p>Username: {req.username}</p>
            <p>Role: {req.role}</p>

            <button onClick={() => approveRequest(req.id)}>
              Approve
            </button>

          </div>
        ))
      }

    </div>
  );
}

export default Dashboard;