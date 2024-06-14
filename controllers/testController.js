const { db } = require("../config/firebase-config");

const testConnection = async (req, res) => {
  try {
    const testDocRef = db.collection("test").doc("testConnection");
    await testDocRef.set({ connected: true });
    const doc = await testDocRef.get();
    if (!doc.exists) {
      res.status(500).json({ message: "Failed to connect to Firestore" });
    } else {
      res
        .status(200)
        .json({
          message: "Successfully connected to Firestore",
          data: doc.data(),
        });
    }
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

module.exports = {
  testConnection,
};
