import { useState } from "react";
import API from "../services/api";

function Signup() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("ROLE_STUDENT");

  const handleSignup = async () => {

    try {

      await API.post("/signup", {
        username,
        password,
        role
      });

      alert("Signup Request Sent");

    } catch (error) {

      alert("Signup Failed");
    }
  };

  return (
    <div style={{ padding: "30px" }}>

      <h1>Signup</h1>

      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />

      <br /><br />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <br /><br />

      <select
        value={role}
        onChange={(e) => setRole(e.target.value)}
      >
        <option value="ROLE_STUDENT">Student</option>
        <option value="ROLE_TEACHER">Teacher</option>
        <option value="ROLE_ADMIN">Admin</option>
      </select>

      <br /><br />

      <button onClick={handleSignup}>Signup</button>

    </div>
  );
}

export default Signup;