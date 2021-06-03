const admin = require("firebase-admin");
const fs = require('fs');

const serviceAccount = require("../Database/sparta-bot-firebase.json");


admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://sparta-bot.firebaseio.com"
});

let db = admin.database();

getMembers();
updateLog();

function getMembers() {

    let members = [];

    db.ref("members").once('value', snapshot => {
        //get all members, put on txt
        snapshot.forEach(member => {
            members.push(member.key);
        })
    }).then(snapshot => {
        fs.appendFile('memberList.txt', members.join("\n"), err => {
            if (err) console.log(err);
            console.log("Written to memberList.txt")
        });
    }).catch(err => {
        console.log("Error reading members: " + err);
    })
}

function updateLog() {
    //read from txt with updates in certain format, update times
    fs.readFile("memberUpdates.txt", 'utf8', async (err, data) => {
        if (err) console.log(err);
        let lines = data.split("\n")
        for (let line of lines) {
            if (line == "" || line == "\r") continue;
            let update = line.split("||");
            update[2] = update[2].replace("\r", "")

            let logRef = db.ref("log/" + update[0] + "/meetings/" + update[1]);
            await logRef.once('value', snap => {
                if (snap.hasChild("start0")) {
                    logRef.child("end0").set(update[2]).catch(err => {
                        console.log("Error updating " + line + ": " + err)
                    })
                } else {
                    logRef.child("start0").set(update[2]).catch(err => {
                        console.log("Error updating " + line + ": " + err)
                    })
                }
            }).catch(err => {
                console.log("Error updating members: " + err)
            })
        }
        console.log("Done updating log")
    })
}
