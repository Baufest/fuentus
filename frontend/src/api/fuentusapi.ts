import axios from "axios";

const fuentusapi = axios.create({
  baseURL: "http://localhost:8080/api/fuentus/"
})

export default fuentusapi;