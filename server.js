const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");

const app = express();
const port = process.env.PORT || 3000;

app.use(cors());
app.use(bodyParser.json());

const authRoutes = require("./routes/authRoutes");
const testRoutes = require("./routes/testRoutes");
const storyRoutes = require("./routes/storyRoutes");

app.use("/auth", authRoutes);
app.use("/api", storyRoutes);
app.use("/test", testRoutes);

app.get("/", (req, res) => {
  res.send("Welcome to LitCove Backend");
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
