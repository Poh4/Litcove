const express = require("express");
const router = express.Router();
const storyController = require("../controllers/storyController");

router.post("/stories", storyController.createStory);
router.get("/stories/:storyId", storyController.getStory);
router.put("/stories/:storyId", storyController.updateStory);
router.delete("/stories/:storyId", storyController.deleteStory);
router.post("/stories/:storyId/comments", storyController.addComment);
router.post("/stories/:storyId/likes", storyController.addLike);

module.exports = router;
