package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Game extends ApplicationAdapter {
	//Variaveis globais//
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoTopo;
	private Texture canoBaixo;
	private Texture gameOver;

	private int estadoJogo = 0;
	private int pontos = 0;
	private int gravidade = 2;
	private float variacao = 0;
	private float posicaoInicialVerticalPassaro = 0;
	private float posicaoCanoHorizontal;
	private float posicaoCanoVertical;
	private float espacoEntreCanos;
	private float larguraDispositivo;
	private float alturaDispositivo;
	private boolean passouCano = false;
	private Random random;

	BitmapFont textoPontuacao;
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;

	private ShapeRenderer shapeRenderer;
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloCanoBaixo;

	@Override
	public void create () {
		//Criando texturas em cena//
		inicializaTexturas();
		//Criando objetos em cena//
		inicializaObjetos();

	}

	private void inicializaObjetos() {
		//variaveis internas//
		random = new Random();
		batch = new SpriteBatch();

		//Obtendo medidas e posicionamentos dos objetos em cena//
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;
		posicaoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 350;

		//Definindo textoPontuacao em tela//
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);

		//Definindo texto em tela//
		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.GREEN);
		textoReiniciar.getData().setScale(3);

		//Definindo texto em tela//
		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.RED);
		textoMelhorPontuacao.getData().setScale(3);

		//Transformando formas em sprites//
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoCima = new Rectangle();
		retanguloCanoBaixo = new Rectangle();

	}

	private void inicializaTexturas() {
		//Pegando texturas seguidas como animação//
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		//Pegando texturas de fundo e canos que se aproximarão//
		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
		gameOver = new Texture("game_over.png");

	}

	@Override

	public void render () {

		verificaEstadoJogo();
		desenharTextura();
		detectarColisao();
		validaPontos();

	}

	private void validaPontos() {
		//definindo regra para somar pontos//
		if(posicaoCanoHorizontal < 50 - passaros[0].getWidth()){
			if(!passouCano){
				pontos++;
				passouCano = true;
			}
		}
		//adicionando variação entre animações//
		variacao += Gdx.graphics.getDeltaTime() * 10;
		if(variacao > 3)
			variacao = 0;
	}

	private void detectarColisao() {

		//Checando a posiçãoe raio de colisão do Passaro e dos canos//
		circuloPassaro.set(50 + passaros[0].getWidth() / 2,
				posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);


		retanguloCanoBaixo.set(posicaoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(),
				canoBaixo.getHeight());

		retanguloCanoCima.set(posicaoCanoHorizontal,
				alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
				canoTopo.getWidth(), canoTopo.getHeight());

		//Checando colisão entre Passaro e canos//
		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		if(bateuCanoBaixo || bateuCanoCima){
			Gdx.app.log("Log", "Bateu");
			estadoJogo = 2;
		}
	}

	private void verificaEstadoJogo() {

		//adicionando regra de botão ao tocar na tela variando estados//
		boolean toqueTela = Gdx.input.justTouched();
		if(estadoJogo == 0) {

			if (Gdx.input.justTouched()) {
				gravidade = -25;
				estadoJogo = 1;
			}
		}else if(estadoJogo == 1){

				if(Gdx.input.justTouched()) {
					gravidade = -25;
				}

				//Configurando regra para canos conforme o jogo passa//
				posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() *200;
				if(posicaoCanoHorizontal < -canoBaixo.getHeight()){
					posicaoCanoHorizontal = larguraDispositivo;
					posicaoCanoVertical = random.nextInt(400) - 200;
					passouCano = false;
				}
				if(posicaoInicialVerticalPassaro > 0 || toqueTela)
					posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

				gravidade++;
			}
				else if(estadoJogo == 2){

				}
		}



	private void desenharTextura(){
		//Definindo posição e formato dos objetos//
		batch.begin();

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int)variacao],50,posicaoInicialVerticalPassaro);
		batch.draw(canoBaixo,posicaoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

		//Apos colisão, carrega mensagens pós gameOver//
		if(estadoJogo == 2){
			batch.draw(gameOver,larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
			textoReiniciar.draw(batch, "TOQUE NA TELA PARA REINICIAR!", larguraDispositivo / 2 - 350, alturaDispositivo / 2 - gameOver.getHeight() / 2);
			textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUAÇÃO É : 0 PONTOS!", larguraDispositivo / 2 - 450, alturaDispositivo / 2 - gameOver.getHeight());
		}
		batch.end();
	}

	@Override
	public void dispose () {

	}
}
