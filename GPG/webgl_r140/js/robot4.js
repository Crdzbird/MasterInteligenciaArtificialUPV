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

var basePositionI;
var basePositionD;

init();
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
    camera.position.set(90, 200, 350);
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    camera2 = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera2.position.set(0, 300, 0);
}

function loadScene() {
    const matRobot = new THREE.MeshBasicMaterial({ color: 'yellow', wireframe: true });

    robot = new THREE.Object3D();
    robot.add(new THREE.Mesh(new THREE.CylinderGeometry(50, 50, 15, 32), matRobot));

    brazo = createBrazo(matRobot);
    robot.add(brazo);

    antebrazo = createAntebrazo(matRobot);
    brazo.add(antebrazo);

    mano = createMano(matRobot);
    antebrazo.add(mano);

    camera2.lookAt(robot.position); 
    scene.add(robot);
    scene.add(new THREE.AxesHelper(1000));
    scene.add(new THREE.Mesh(new THREE.PlaneGeometry(1000, 1000, 50, 50), matRobot).rotateX(-Math.PI / 2));
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
    const mano = new THREE.Mesh(new THREE.CylinderGeometry(15, 15, 40, 32), material);
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
    pinzas.pinzaI = new THREE.Mesh(geopinza, material);
    pinzas.pinzaI.rotateY(Math.PI / 2);
    mano.add(pinzas.pinzaI);

    pinzas.pinzaD = new THREE.Mesh(geopinza, material);
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
