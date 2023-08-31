const functions = require("firebase-functions");
const admin = require("firebase-admin");
const path = require("path");
const {log} = require("console");

const adminSdkFilePath = path.join(__dirname, "deneme-295ac-ae4777ba080c.json");

try {
  admin.initializeApp({
    credential: admin.credential.cert(adminSdkFilePath),
    databaseURL: "https://deneme-295ac-default-rtdb.europe-west1.firebasedatabase.app",
    project_id: "deneme-295ac",
  });

  exports.sendNotification = functions.database
      .ref("/mesajlar/{mesajID}/" + "mesajlar/{mesaj}")
      .onCreate(async (snapshot, context) => {
        const mesajID = context.params.mesajID;
        const mesaj = snapshot.child("mesaj").val();
        const gonderen = snapshot.child("gonderen").val();
        log("Yeni mesaj ID:", mesajID);
        log("Mesaj içeriği:", mesaj);

        try {
          const snapg = await admin.database()
              .ref("/users/" + gonderen).once("value");
          const aladsoyad = snapg.child("ad")
              .val() + " " + snapg.child("soyad").val();

          const snap = await admin.database().ref("/mesajlar/" + mesajID +
           "/kisiler/" + gonderen).once("value");
          const alici = snap.val();
          log("ALICI: ", alici);
          log("GÖNDEREN", gonderen);

          const tokensnapshot = await admin.database()
              .ref("/users/" + alici).once("value");
          const fcmToken = tokensnapshot.child("fcmToken").val();

          const payload = {
            notification: {
              title: "Yeni Mesaj: " + aladsoyad,
              body: mesaj,
              gonderenkey: gonderen,
              click_action: "OPEN_ACTIVITY",
            },
          };

          await admin.messaging().sendToDevice(fcmToken, payload);
        } catch (error) {
          log("ERROR: ", error);
        }
      });
} catch (parseError) {
  console.error("JSON çözme hatası:", parseError);
}
