# Discovery World GAN Testing Website

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Project Structure

This project is split into two sections: the client and the server. In order to have both running, you must first build
the client with the `npm run build` command, then start the server with the `npm start` command. Make sure to run the 
start command from the root directory of this project. Running `npm start` from the `client/` subdirectory will only
start the front end.

## Available Scripts

In the project, you can run:

### `npm start`

If run from the project root directory, this runs the app in production mode.\
The version of the app will be what was most recently built to the `client/build/` directory.\
Open [http://localhost:3001](http://localhost:3001) to view it in the browser.

If run from the `client/` subdirectory, this runs only the front end in developer mode, so the server will not be running.\
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits in developer mode.\
You will also see any lint errors in the console.

### `npm run build`

Builds the app for production to the `client/build/` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

# GAN Setup

All of these command must be run in the `server` folder.

## Linux

```bash
sudo apt install python3-pip python3-venv
python3 -m pip install --user virtualenv
python3 -m venv venv
source venv/bin/activate
python3 -m pip install torch torchvision watchdog
```

At this point, the app can be run with a simple `python3 main.py -l`.

## Windows

```cmd
py -m pip install --user virtualenv
py -m venv venv
.\venv\Scripts\activate
py -m pip install torch torchvision watchdog
```

At this point, the app can be run with a simple `py main.py -l`.
