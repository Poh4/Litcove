const express = require("express");
const router = express.Router();
const testController = require("../controllers/testController");

router.get("/test-connection", testController.testConnection);

module.exports = router;
