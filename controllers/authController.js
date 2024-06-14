const { auth, db } = require("../config/firebase-config");
const bcrypt = require("bcrypt");

const register = async (req, res) => {
  const { email, password, username, bio, profilePicture } = req.body;
  try {
    const userRecord = await auth.createUser({
      email,
      password,
    });

    const passwordHash = await bcrypt.hash(password, 10);

    await db.collection("users").doc(userRecord.uid).set({
      username,
      email,
      passwordHash,
      bio,
      profilePicture,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    res
      .status(201)
      .json({ message: "User registered successfully", uid: userRecord.uid });
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
};

const login = async (req, res) => {
  const { email, password } = req.body;
  try {
    const userRecord = await auth.getUserByEmail(email);

    const userDoc = await db.collection("users").doc(userRecord.uid).get();
    if (!userDoc.exists) {
      return res.status(404).json({ message: "User not found" });
    }

    const user = userDoc.data();
    const isPasswordValid = await bcrypt.compare(password, user.passwordHash);
    if (!isPasswordValid) {
      return res.status(401).json({ message: "Invalid password" });
    }

    const token = await auth.createCustomToken(userRecord.uid);
    res.status(200).json({ token });
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
};

const logout = async (req, res) => {
  const { uid } = req.body;
  try {
    await auth.revokeRefreshTokens(uid);
    res.status(200).json({ message: "User logged out successfully" });
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
};

module.exports = {
  register,
  login,
  logout,
};
