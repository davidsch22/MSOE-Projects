const express = require('express');
const path = require("path");
const multer = require("multer");
const morgan = require("morgan");
const fs = require("fs");

const app = express();

const PORT = process.env.PORT || 3001;

// Have Node serve the files for our built React app
app.use(express.json());
app.use(express.urlencoded({extended: true}));
app.use(morgan('dev'));
app.use(express.static(path.resolve(__dirname, '../client/build')));

const storage = multer.diskStorage({
    destination: function (req, file, callback) {
        callback(null, __dirname + '/in/')
    },
    filename: function (req, file, callback) {
        callback(null, "CycleGAN-style_monet_pretrained." + Date.now() + '.png')
    }
})

const upload = multer({ storage: storage });

app.post("/upload", upload.single('file'),
    (req, res) => {
        if (!req.file) {
            console.log("No file received");
            return res.status(400).send({
                success: false
            });
        } else {
            console.log('file received');

            const filename = req.file.filename;

            while (true) { // Probably really inefficient way to wait until the GAN makes the result
                if (fs.existsSync(__dirname + "/out/" + filename)) {
                    return res.send({
                        success: true,
                        filename: filename
                    });
                }
            }
        }
    }
);

app.get("/images", (req, res) => {
    const fileName = req.query.name;
    const type = req.query.type;
    let directoryPath = __dirname;
    if (type === "in") {
        directoryPath += "/in/";
    } else if (type === "out") {
        directoryPath += "/out/";
    }
    const filePath = directoryPath + fileName;

    fs.access(filePath, (err) => {
        if (err) {
            res.status(404).send({
                success: false,
                message: err
            });
        }

        res.writeHead(200, {
            "Content-Type": "image/png"
        });

        fs.readFile(filePath, (err, data) => {
            res.end(data);
        });
    });
});

app.post("/rate", (req, res) => {
    if (!req.body) {
        console.log("No data received");
        return res.send({
            success: false
        });
    } else {
        console.log(req.body);
        let data = JSON.stringify(req.body);
        fs.writeFileSync(__dirname + '/ratings/' + Date.now() + '.json', data);
        return res.send({
            success: true
        });
    }
});

// All other GET requests not handled before will return our React app
app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname, '../client/build', 'index.html'));
});

app.listen(PORT, () => {
    console.log(`Server is listening on port ${PORT}`);
});