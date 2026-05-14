import { useNavigate } from "react-router-dom";

function Home() {

  const navigate = useNavigate();

  const logout = () => {

    localStorage.removeItem("token");

    navigate("/login");
  };

  return (
    <div style={{ padding: "30px" }}>

      <h1>Welcome Secure User 🔥</h1>

      <button onClick={logout}>Logout</button>

    </div>
  );
}

export default Home;