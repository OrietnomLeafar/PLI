/**
 * Classe RendererImagem, junto com a classe ExemploImagem, mostra um exemplo de 
 * como trabalhar com imagens em OpenGL utilizando a API JOGL.
 * 
 * @author Marcelo Cohen, Isabel H. Manssour 
 * @version 1.0
 */

import java.awt.event.*; 
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import java.nio.FloatBuffer;

public class RendererImagem extends MouseAdapter implements GLEventListener, KeyListener
{
	// Atributos
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private double fAspect;
	private Imagem imgs[], nova;
	private int sel;
	
	/**
	 * Construtor da classe RendererImagem que recebe um array com as imagens
	 */
	public RendererImagem(Imagem imgs[])
	{
		// Inicializa o valor para corre�ão de aspecto   
		fAspect = 1;

		// Imagem carregada do arquivo
		this.imgs = imgs;
		nova = null;
		sel = 0;	// selecionada = primeira imagem
	}

	/**
	 * M�todo definido na interface GLEventListener e chamado pelo objeto no qual ser� feito o desenho
	 * logo ap�s a inicializa��o do contexto OpenGL. 
	 */    
	public void init(GLAutoDrawable drawable)
	{
		glDrawable = drawable;
		gl = drawable.getGL();
		// glu = drawable.getGLU();       
		glu = new GLU();
		glut = new GLUT();

		drawable.setGL(new DebugGL(gl));        

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		// Define a janela de visualiza�ão 2D
		gl.glMatrixMode(GL.GL_PROJECTION);
		glu.gluOrtho2D(0,1,0,1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}

	/**
	 * M�todo definido na interface GLEventListener e chamado pelo objeto no qual ser� feito o desenho
	 * para come�ar a fazer o desenho OpenGL pelo cliente.
	 */  
	public void display(GLAutoDrawable drawable)
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();    

		gl.glColor3f(0.0f, 0.0f, 1.0f);

		// Desenha a imagem original
		gl.glRasterPos2f(0,0);
		gl.glDrawPixels(imgs[sel].getWidth(), imgs[sel].getHeight(),
			       GL.GL_BGR, GL.GL_UNSIGNED_BYTE, imgs[sel].getData());

		// Desenha a imagem resultante
		if(nova!=null) {
			gl.glRasterPos2f(0.5f,0);
			gl.glDrawPixels(nova.getWidth(), nova.getHeight(),
			       GL.GL_BGR, GL.GL_UNSIGNED_BYTE, nova.getData());
		}
	}

	/**
	 * M�todo definido na interface GLEventListener e chamado pelo objeto no qual será feito o desenho
	 * depois que a janela foi redimensionada.
	 */  
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		gl.glViewport(0, 0, width, height);
		fAspect = (float)width/(float)height;      
	}

	/**
	 * M�todo definido na interface GLEventListener e chamado pelo objeto no qual será feito o desenho
	 * quando o modo de exibi�ão ou o dispositivo de exibi�ão associado foi alterado.
	 */  
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) { }

	/**
	 * M�todo da classe MouseAdapter que está sendo sobrescrito para gerenciar os 
	 * eventos de clique de mouse, de maneira que seja feito zoom in e zoom out.
	 */  
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) // botão 1
		{ }
		if (e.getButton() == MouseEvent.BUTTON3) // botão 2
		{ }
		glDrawable.display();
	}

	/**
	 * M�todo definido na interface KeyListener que está sendo implementado para, 
	 * de acordo com as teclas pressionadas, permitir mover a posi�ão do observador
	 * virtual.
	 */        
	public void keyPressed(KeyEvent e)
	{
		// F1 para próxima imagem
		if(e.getKeyCode()==KeyEvent.VK_F1)
		{
			if(++sel>imgs.length-1) sel=imgs.length-1;
		}
		// F2 para imagem anterior
		else if(e.getKeyCode()==KeyEvent.VK_F2)
		{
			if(--sel<0) sel = 0;
		}

		// Cria a imagem resultante
		nova = (Imagem) imgs[sel].clone();

		switch (e.getKeyCode())
		{
			case KeyEvent.VK_1:		// Para exibir a imagem "original": não faz nada
										System.out.println("Negative");
										negative();
										break;
			case KeyEvent.VK_2:		// Para converter a imagem para tons de cinza
										System.out.println("Grayscale");
										convertToGrayScale();
										break;     
			case KeyEvent.VK_3:		// Para converter a imagem para preto e branco  
										System.out.println("B&W");
										convertToGrayScale();
										convertToBlackAndWhite();
										break;
			case KeyEvent.VK_4:		// Para aplicar um filtro passa-alta (realce de bordas)
										System.out.println("High pass");
										convertToGrayScale();
										highPass();
										break;
			case KeyEvent.VK_5:		// Para aplicar um filtro passa-baixa (suavização)
										System.out.println("Low pass");
										convertToGrayScale();
										lowPass();
										break;
			case KeyEvent.VK_6:		// Para gerar a imagem atrav�s da t�cnica de posteriza�ão
										System.out.println("Posterize");
										posterize();
										break; 	
			case KeyEvent.VK_7:		// Para converter a imagem para tons de s�pia  
										System.out.println("Sepia");
										convertToGrayScale();
										sepia();
										break; 
			case KeyEvent.VK_ESCAPE:	System.exit(0);
										break;
		}  
		glDrawable.display();
	}

	/**
	 * M�todo definido na interface KeyListener.
	 */      
	public void keyTyped(KeyEvent e) { }

	/**
	 * M�todo definido na interface KeyListener.
	 */       
	public void keyReleased(KeyEvent e) { }
    
	/**
	 * M�todo que converte a imagem para tons de cinza.
	 */       
	public void convertToGrayScale() 
	{ 
		// Tarefa 1:
		//		Gerar uma imagem em tons de cinza 
		//		Use os m�todos 
		//			getPixel/getR/getG/getB e setPixel da classe Imagem
		// 		Altere apenas o atributo nova.
		//     Experimente executar e testar nas imagens disponibilizadas.
		
		int R=0, G=0, B=0;
		int cinza = 0;
		
		for (int i = 0; i < nova.getWidth(); i++) {
			for (int j = 0; j < nova.getHeight(); j++) {
				R = nova.getR(i, j);
				G = nova.getG(i, j);
				B = nova.getB(i, j);
				
				cinza = (R+G+B)/3;
				
				nova.setPixel(i, j, cinza, cinza, cinza);
			}
		}
		
		System.out.println(nova.getWidth()+" "+nova.getHeight());

	}    

	/**
	 * M�todo que converte a imagem de tons de cinza para preto e branco.
	 */       
	public void convertToBlackAndWhite() 
	{ 
		// Tarefa 2:
		//		Uma forma de segmentar/transformar as imagens para P&B � a
		//		limiariza���o (thresholding). Se o valor do pixel for menor que o valor do
		//		limiar a cor deve ser preta, caso contr�rio deve ser branca.
		//		Implemente essa t�cnica e varie o valor do limiar para 
		//		n�meros pequenos como 10 ou 15 e n�meros grandes como 100 e 150.
		//		Altere apenas o atributo nova.
		//      Experimente executar e testar nas imagens disponibilizadas.

		// Tarefa 3:
		//		Agora apenas mude o valor do limiar da tarefa 2 para um n�mero 
		//		que represente a m�dia entre os valores de densidade encontrados 
		//		na imagem. Altere apenas o atributo nova.
		//      Experimente executar e testar nas imagens disponibilizadas.		
		
		int media = 0;
		int wid = nova.getWidth();
		int hei = nova.getHeight();
		
		for (int i = 0; i < wid; i++) {
			for (int j = 0; j < hei; j++) {
				media += nova.getB(i, j);
			}
		}
		
		media = media /(wid*hei);
		
		for (int i = 0; i < wid; i++) {
			for (int j = 0; j < hei; j++) {
				if(nova.getB(i, j) > media){
					nova.setPixel(i, j, 0, 0, 0);
					
				}else{
					nova.setPixel(i, j, 255, 255, 255);
				}
			}
		}
	}
	
	public void negative(){
		int actR = 0,actG = 0,actB = 0;
		int newR, newG, newB;
		
		int wid = nova.getWidth();
		int hei = nova.getHeight();
		
		for (int i = 0; i < wid; i++) {
			for (int j = 0; j < hei; j++) {
				actR = nova.getR(i, j);
				actG = nova.getG(i, j);
				actB = nova.getB(i, j);
				
				newR = 255 - actR;
				newG = 255 - actG;
				newB = 255 - actB;
				
				nova.setPixel(i, j, newR, newG, newB);
			}
		}
	}

	/**
	 * M�todo para aplicar um kernel (filtro) qualquer a uma imagem
	 * Deve receber uma matrix 3x3, e aplica este à imagem original,
	 * alterando a imagem "nova"
	 */
	public void applyKernel(float [][]vert, float[][] hor){
		int count =0;
		int wid = nova.getWidth();
		int hei = nova.getHeight();
		int [][]auxV = new int[wid][hei];
		int [][]auxH = new int[wid][hei];
		double G = 0;
		double alfa = 5.6;
		double T = 0;
		int max =0;
		
		for(int x=0; x<wid-2; x++){
			for(int y=0; y<hei-2; y++){
				
				int somav = 0;
				int somah = 0;
				
				for(int i=0; i<3; i++){
					for(int j=0; j<3; j++){
						int p = nova.getR(x+i,y+j);
						
						somav += p * vert[i][j];
						somah += p * hor[i][j]; 
						
						if(count<20){
							System.out.println(p +" "+ vert[i][j]);
							if(somav>max){
								max = somav;
							}
							if(i+j == 4){
								System.out.println(somav);
								System.out.println();
							}else{
								System.out.println("parcial: "+somav);
							}
						}
						
					}
				}
				count++;
				
				auxV[x+1][y+1] =(int) somav;

				auxH[x+1][y+1] =(int) somah;
		
				double r = (somah * somah) + (somav * somav);
				r = Math.sqrt(r);
				
				G += r;
			}
		}
		System.out.println("MAX "+max);
		System.out.println("G "+G);
		G = G/(wid*hei);
		System.out.println("Gb "+ G);
		
		for (int i = 0; i < wid; i++) {
			for (int j = 0; j < hei; j++) {
				nova.setPixel(i, j, auxV[i][j] , auxV[i][j] , auxV[i][j]);
			}
		}
	}

	/**
	 * M�todo para aplicar um filtro passa-alta
	 */
	public void highPass()
	{
		// Tarefa 4:
		//		Implemente um filtro passa-alta (realce de bordas). 
		//		Defina uma matriz 3x3 com os valores corretos e chame o m�todo applyKernel.
		//		ex: float[][] matriz = { 0, 0, 0 } , { 0 , 0, 0 } , { 0, 0 ,0 }};
		//		    applyKernel(matriz);
		
		
		float[][] vertical = {{1,2,1}, {0,0,0}, {-1,-2,-1}};
		float[][] horizontal = {{-1,0,1}, {-2,0,2}, {-1,0,1}};
		
		applyKernel(vertical, horizontal);
		
	}

	/**
	 * M�todo para aplicar um filtro passa-baixa
	 */
	public void lowPass()
	{
		// Tarefa 5:
		//		Implemente um filtro passa-baixa (suaviza��o). 
		//		Defina uma matriz 3x3 com os valores corretos e chame o m�todo applyKernel.
		//		ex: float[][] matriz = { 0, 0, 0 } , { 0 , 0, 0 } , { 0, 0 ,0 }};
		//		    applyKernel(matriz);		
	}

	/**
	 * M�todo que altera a imagem atrav�s da t�cnica de posteriza��o.
	 */       
	public void posterize() 
	{ 
		// Tarefa 6:
		//		Gere uma imagem atrav�s da t�cnica de posteriza��o, que simplesmente 
		//		reduz a quantidade de tons diferentes de cada componente de cor. Por 
		//		exemplo, se quisermos posterizar para 10 intensidades, 25 tons de cada 
		//		componente se transformam no tom m�dio de cada intervalo 
		//		(0..25 = 12, 26..50 = 38, ...). 
		//   Experimente executar e testar nas imagens disponibilizadas.
		
	}	
	
	/**
	 * M�todo que converte a imagem para tons de s�pia.
	 */       
	public void sepia() 
	{ 
		// Tarefa 7:
		//		Gere uma imagem em tons de s�pia, atrav�s do seguinte algoritmo:
		//		- A imagem deve estar ema tons de cinza; 
		//		- Fa�a as sombras (cinzas mais escuros) ficarem ainda mais escuras (0 <= cinza < 60); 
		//		- Fa�a os cinzas m�dios ficarem meio amarronzados (60 <= cinza < 190), reduzindo o azul; 
		//		- Fa�a as partes claras (cinzas claros) ficarem meio amarelados (190 <= cinza):
		//			aumente vermelho e verde
		//			ou diminua o azul. 
		//      Experimente executar e testar nas imagens disponibilizadas.		


	}		
}

