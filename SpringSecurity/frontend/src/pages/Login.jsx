import { useState } from "react";
import API from "../services/api";
import { useNavigate } from "react-router-dom";

function Login() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleLogin = async () => {

    try {

      const response = await API.post(
        `/login?username=${username}&password=${password}`
      );

      localStorage.setItem("token", response.data);

      alert("Login Successful");

      navigate("/home");

    } catch (error) {

      alert("Login Failed");
    }
  };

  return (
    <div style={{ padding: "30px" }}>

      <h1>Login</h1>

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

      <button onClick={handleLogin}>Login</button>

    </div>
  );
}

export default Login;