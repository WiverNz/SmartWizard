package com.smartwizard.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

public class MGdxGame extends ApplicationAdapter implements InputProcessor {
	public PerspectiveCamera cam;
	public Model model;
	public ModelInstance modelInstance;
	public ModelBatch modelBatch;
	public Environment environment;
	public CameraInputController camController;
	AnimationController animController;
	public AssetManager assets;
	boolean loading;
	InputMultiplexer multiplexer;
	float z_coord = 0f;
	boolean moving = false;
	
	@Override
	public void create () {
		assets = new AssetManager();
		assets.load("data/hjmediastudios_sintel_ultralite.g3db", Model.class);
		loading = true;
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(500f, 500f, 500f);
		cam.lookAt(0f, 0f, 0f);
		cam.near = 1f;
		cam.far = 3000f;
		cam.update();

		multiplexer = new InputMultiplexer();
		camController = new CameraInputController(cam);
		multiplexer.addProcessor(camController);
		multiplexer.addProcessor(this);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public void doneLoading(){
		model = assets.get("data/hjmediastudios_sintel_ultralite.g3db", Model.class);
		modelInstance = new ModelInstance(model);
		modelInstance.transform.setToTranslation(0, 0, z_coord);
		animController = new AnimationController(modelInstance);
		animController.setAnimation("Sintel Ultralite|Stay", -1);
		loading = false;
	}

	@Override
	public void render () {
		if(loading&&assets.update()){
			doneLoading();
		}
		if(moving){
			z_coord += 0.02;
			modelInstance.transform.setToTranslation(0, 0, z_coord);
		}
		camController.update();

		Gdx.gl.glViewport ( 0 , 0 , Gdx.graphics.getWidth (), Gdx.graphics.getHeight ());
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

		if(!loading){
			modelBatch.begin(cam);
			modelBatch.render(modelInstance, environment);
			modelBatch.end();
			animController.update(Gdx.graphics.getDeltaTime());
		}
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		model.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.UP){
			animController.setAnimation("Sintel Ultralite|Move", -1);
			moving = true;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.UP){
			animController.setAnimation("Sintel Ultralite|Stay", -1);
			moving = false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
