package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	//Variaveis globais//
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoTopo;
	private Texture canoBaixo;

	private float larguraDispositivo;
	private float alturaDispositivo;
	private int movimentaY = 0;
	private int movimentaX = 0;

	private float variacao = 0;
	private  float gravidade = 0;
	private  float posicaoInicialVerticalPassaro = 0;
	
	@Override
	public void create () {
		//Criando objetos em cena//
		batch = new SpriteBatch();
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		fundo = new Texture("fundo.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;

	}

	@Override

	public void render () {
		batch.begin();
		//adicionando variação entre animações//
		if(variacao > 3)
			variacao = 0;

		//adicionando regra de botão ao tocar na tela//
		boolean toqueTela = Gdx.input.justTouched();
		if(Gdx.input.justTouched()){
			gravidade = -25;
		}

		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		//Definindo posição e formato dos objetos//
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int)variacao],30,posicaoInicialVerticalPassaro);

		variacao += Gdx.graphics.getDeltaTime() * 10;

		//adicionando movimentação e fisica//
		gravidade++;
		movimentaX++;
		movimentaY++;
		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
