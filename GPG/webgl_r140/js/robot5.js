var renderer, scene, camera, camera2, robot, brazo, antebrazo, mano;
var controls = {
    robotRotationY: 0,
    brazoRotationX: 0,
    antebrazoRotationX: 0,
    antebrazoRotationY: 0,
    manoRotationX: 0,
    pinzasSeparation: 0
};
var pinzas = {
    pinzaI: null,
    pinzaD: null
};

var basePositionI, basePositionD;

init();
generateLights();
loadScene();
setupGUI();
render();

function init() {
    renderer = new THREE.WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.setClearColor(new THREE.Color(0x0000AA));
    document.getElementById('container').appendChild(renderer.domElement);

    scene = new THREE.Scene();

    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera.position.set(90, 300, 350);
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    camera2 = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera2.position.set(0, 300, 0);
}

function generateLights() {
    const lights = [
        { type: 'AmbientLight', color: "white", intensity: 0.7 },
        { type: 'DirectionalLight', color: "white", intensity: 0.5, position: { x: 0, y: 1, z: -3 } },
        { type: 'PointLight', color: "blue", intensity: 15, distance: 150, position: { x: -450, y: 40, z: -500 } },
        { type: 'SpotLight', color: "white", intensity: 2, distance: 1200, position: { x: 800, y: 800, z: 600 }, target: new THREE.Vector3(0, 0, 0), angle: Math.PI / 10, penumbra: 1, castShadow: true },
        { type: 'SpotLight', color: "white", intensity: 1, distance: 1800, position: { x: -500, y: 800, z: 600 }, target: new THREE.Vector3(0, 0, 0), angle: Math.PI / 10, penumbra: 1, castShadow: true }
    ];

    lights.forEach(lightData => {
        const light = new THREE[lightData.type](lightData.color, lightData.intensity, lightData.distance);
        if (lightData.position) light.position.set(lightData.position.x, lightData.position.y, lightData.position.z);
        if (lightData.target) light.target.position.set(lightData.target.x, lightData.target.y, lightData.target.z);
        if (lightData.angle) light.angle = lightData.angle;
        if (lightData.penumbra) light.penumbra = lightData.penumbra;
        if (lightData.castShadow) light.castShadow = true;
        scene.add(light);
    });
}

function loadTexture(path, repeatX = 1, repeatY = 1) {
    const texture = new THREE.TextureLoader().load(path);
    texture.wrapS = THREE.RepeatWrapping;
    texture.wrapT = THREE.RepeatWrapping;
    texture.repeat.set(repeatX, repeatY);
    return texture;
}

function loadScene() {
    const metalTexture = loadTexture("../webgl_r140/images/metal_128.jpg");
    const woodTexture = loadTexture("../webgl_r140/images/wood512.jpg", 4, 4);
    const posxTexture = loadTexture("../webgl_r140/images/posx.jpg");
    const poszTexture = loadTexture("../webgl_r140/images/posz.jpg");

    const metalMaterial = new THREE.MeshLambertMaterial({ color: "white", specular: "white", shininess: 15, map: metalTexture, wireframe: false });
    const woodMaterial = new THREE.MeshLambertMaterial({ color: "white", specular: "white", shininess: 15, map: woodTexture, wireframe: false });
    const posxMaterial = new THREE.MeshPhongMaterial({ color: "white", specular: "white", shininess: 15, map: posxTexture, wireframe: false });
    const poszMaterial = new THREE.MeshPhongMaterial({ color: "white", specular: "white", shininess: 15, map: poszTexture, wireframe: false });

    robot = new THREE.Object3D();
    robot.add(new THREE.Mesh(new THREE.CylinderGeometry(50, 50, 15, 32), metalMaterial));

    let pared1 = new THREE.Mesh(new THREE.PlaneGeometry(2000, 2000, 25, 25), posxMaterial);
    pared1.position.z = -500;
    robot.add(pared1);

    let pared3 = new THREE.Mesh(new THREE.PlaneGeometry(2000, 2000, 25, 25), poszMaterial);
    pared3.position.set(-600, 0, -200);
    pared3.rotation.y = Math.PI / 2;
    robot.add(pared3);

    robot.rotation.y = -35 * (Math.PI / 180);

    brazo = createBrazo(metalMaterial);
    robot.add(brazo);

    antebrazo = createAntebrazo(metalMaterial);
    brazo.add(antebrazo);

    mano = createMano(woodMaterial);
    antebrazo.add(mano);

    camera2.lookAt(robot.position);
    scene.add(robot);
    scene.add(new THREE.AxesHelper(1000));
    scene.add(new THREE.Mesh(new THREE.PlaneGeometry(1000, 1000, 50, 50), woodMaterial).rotateX(-Math.PI / 2));
}


function createBrazo(material) {
    const brazo = new THREE.Object3D();
    const components = [
        { geometry: new THREE.CylinderGeometry(20, 20, 18, 32), rotation: [0, 0, Math.PI / 2] },
        { geometry: new THREE.BoxGeometry(18, 120, 12), position: [0, 50, 0], rotation: [0, Math.PI / 2, 0] },
        { geometry: new THREE.SphereGeometry(20, 30, 15), position: [0, 120, 0] }
    ];

    components.forEach(comp => {
        const mesh = new THREE.Mesh(comp.geometry, material);
        if (comp.position) mesh.position.set(...comp.position);
        if (comp.rotation) mesh.rotation.set(...comp.rotation);
        brazo.add(mesh);
    });

    return brazo;
}

function createAntebrazo(material) {
    const antebrazo = new THREE.Object3D();
    antebrazo.position.set(0, 120, 0);

    const components = [
        { geometry: new THREE.CylinderGeometry(22, 22, 6, 32) },
        ...Array(4).fill().map((_, i) => ({
            geometry: new THREE.BoxGeometry(4, 80, 4),
            position: [8 * (i % 2 === 0 ? 1 : -1), 34, 4 * (Math.floor(i / 2) === 0 ? -1 : 1)]
        }))
    ];

    components.forEach(comp => {
        const mesh = new THREE.Mesh(comp.geometry, material);
        if (comp.position) mesh.position.set(...comp.position);
        antebrazo.add(mesh);
    });

    return antebrazo;
}

function createMano(material) {
    const texture = new THREE.TextureLoader().load("../webgl_r140/images/wood512.jpg");
    texture.wrapS = THREE.RepeatWrapping;
    texture.wrapT = THREE.RepeatWrapping;
    texture.repeat.set(1, 1);

    let materialMetal = new THREE.MeshLambertMaterial({
        color: "white",
        specular: "white",
        shininess: 15,
        map: texture,
        wireframe: false
    });
    const mano = new THREE.Mesh(new THREE.CylinderGeometry(15, 15, 40, 32), materialMetal);
    mano.position.set(0, 70, 5);
    mano.rotateZ(Math.PI / 2);

    const geopinza = new THREE.BufferGeometry();
    const vertices = [
        0, -8, -10,
        19, -8, -10,
        0, -8, 10,
        19, -8, 10,
        0, -12, -10,
        19, -12, -10,
        0, -12, 10,
        19, -12, 10,
        38, -8, -5,
        38, -12, -5,
        38, -8, 5,
        38, -12, 5
    ];
    const indices = [
        0, 3, 2,
        0, 1, 3,
        1, 7, 3,
        1, 5, 7,
        5, 6, 7,
        5, 4, 6,
        4, 2, 6,
        4, 0, 2,
        2, 7, 6,
        2, 3, 7,
        4, 1, 0,
        4, 5, 1,
        1, 10, 3,
        1, 8, 10,
        8, 11, 10,
        8, 9, 11,
        9, 7, 11,
        9, 5, 7,
        3, 11, 7,
        3, 10, 11,
        5, 8, 1,
        5, 9, 8
    ];
    
    geopinza.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3));
    geopinza.setIndex(indices);
    pinzas.pinzaI = new THREE.Mesh(geopinza, materialMetal);
    pinzas.pinzaI.rotateY(Math.PI / 2);
    mano.add(pinzas.pinzaI);

    pinzas.pinzaD = new THREE.Mesh(geopinza, materialMetal);
    pinzas.pinzaD.rotateY(Math.PI / 2);
    pinzas.pinzaD.position.set(0, 20, 0);
    mano.add(pinzas.pinzaD);

    basePositionI = pinzas.pinzaI.position.y;
    basePositionD = pinzas.pinzaD.position.y;

    return mano;
}

function render() {
    requestAnimationFrame(render);

    renderer.setViewport(0, 0, window.innerWidth, window.innerHeight);
    renderer.setScissor(0, 0, window.innerWidth, window.innerHeight);
    renderer.setScissorTest(true);
    renderer.render(scene, camera);

    const smallViewportSize = { width: window.innerWidth * 0.2, height: window.innerHeight * 0.2 };
    renderer.setViewport(0, window.innerHeight - smallViewportSize.height, smallViewportSize.width, smallViewportSize.height);
    renderer.setScissor(0, window.innerHeight - smallViewportSize.height, smallViewportSize.width, smallViewportSize.height);
    renderer.setScissorTest(true);
    renderer.render(scene, camera2);

    renderer.setViewport(0, 0, window.innerWidth, window.innerHeight);
    renderer.setScissor(0, 0, window.innerWidth, window.innerHeight);
    renderer.setScissorTest(false);
}

function setupGUI() {
    var gui = new dat.GUI();
    addGuiControl(gui, 'robotRotationY', robot, 'rotation', 'y', 'Robot Rotation Y');
    addGuiControl(gui, 'brazoRotationX', brazo, 'rotation', 'x', 'Brazo Rotation X');
    addGuiControl(gui, 'antebrazoRotationX', antebrazo, 'rotation', 'x', 'Antebrazo Rotation X');
    addGuiControl(gui, 'antebrazoRotationY', antebrazo, 'rotation', 'y', 'Antebrazo Rotation Y');
    addGuiControl(gui, 'manoRotationX', mano, 'rotation', 'x', 'Mano Rotation Y');
    gui.add(controls, 'pinzasSeparation', -7, 7).step(0.01).name('Pinzas Separation').onChange(function(value) {
        pinzas.pinzaI.position.y = basePositionI + value;
        pinzas.pinzaD.position.y = basePositionD - value;
    });
}

function addGuiControl(gui, controlName, object, property, axis, displayName) {
    gui.add(controls, controlName, -Math.PI, Math.PI).step(0.01).name(displayName).onChange(function(value) {
        object[property][axis] = value;
    });
}
