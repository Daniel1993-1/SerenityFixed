const { initializeTestEnvironment, assertFails, assertSucceeds } = require('@firebase/rules-unit-testing');
const fs = require('fs');
const path = require('path');

async function runTests() {
  console.log("Conectando al emulador...");

  const testEnv = await initializeTestEnvironment({
    projectId: "serenityappmovil", 
    firestore: {
      host: "127.0.0.1",
      port: 8080,
      rules: fs.readFileSync(path.resolve(__dirname, "../firestore.rules"), "utf8"),
    },
  });

  const alice = testEnv.authenticatedContext("alice");
  const bob = testEnv.authenticatedContext("bob");
  const unauth = testEnv.unauthenticatedContext();

  console.log("Iniciando pruebas de seguridad (S-01 a S-07)...\n");

  // S-01: Sin sesión intenta leer datos de Alice
  try { await assertFails(unauth.firestore().doc('/users/alice').get()); console.log("✅ S-01: PASS"); } catch (e) { console.log("❌ S-01: FAIL"); }

  // S-02: Alice lee sus propios datos
  try { await assertSucceeds(alice.firestore().doc('/users/alice').get()); console.log("✅ S-02: PASS"); } catch (e) { console.log("❌ S-02: FAIL"); }

  // S-03: Alice intenta leer datos de Bob
  try { await assertFails(alice.firestore().doc('/users/bob').get()); console.log("✅ S-03: PASS"); } catch (e) { console.log("❌ S-03: FAIL"); }

  // S-04: Alice intenta escribir en el espacio de Bob
  try { await assertFails(alice.firestore().doc('/users/bob/evaluations/test').set({ score: 10 })); console.log("✅ S-04: PASS"); } catch (e) { console.log("❌ S-04: FAIL"); }

  // S-05: Alice intenta listar toda la colección de usuarios
  try { await assertFails(alice.firestore().collection('/users').get()); console.log("✅ S-05: PASS"); } catch (e) { console.log("❌ S-05: FAIL"); }

  // S-06: Alice intenta acceder a una ruta no prevista
  try { await assertFails(alice.firestore().doc('/admin/secrets').get()); console.log("✅ S-06: PASS"); } catch (e) { console.log("❌ S-06: FAIL"); }

  // S-07: Alice consulta su historial (no debería ver el de Bob)
  try { await assertSucceeds(alice.firestore().collection('/users/alice/evaluations').get()); console.log("✅ S-07: PASS"); } catch (e) { console.log("❌ S-07: FAIL"); }

  await testEnv.cleanup();
  console.log("\nPruebas finalizadas.");
}

runTests().catch(console.error);