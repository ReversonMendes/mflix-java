package mflix.api.daos;
import java.util.Scanner;

public class teste {


    public static class Operadores{
        public static void main(String[]args){
            Scanner scanner = new Scanner(System.in);


            int a = 0;
            int b = 0;

            System.out.println(" Digite o valor de A: ");
            a = scanner.nextInt();

            System.out.println(" Digite o valor de B: ");
            b = scanner.nextInt();

            int soma = a + b;
            int subtracao = a - b;
            int multiplicacao = a * b;
            int resto = 0;

            //Só faz se o valor de B for diferente de 0 eu poderia utilizar o ' > 0 ' que também funcionaria
            if(b != 0){
                resto = a % b;
            }



            System.out.println(soma);
            System.out.println(subtracao);
            System.out.println(multiplicacao);

            //se for zero manda mensagem! senão apresenta o mod
            if( b==0 ){
                System.out.println("Não é possivel dividir por zero! valor de B = " + b);
            }else {
                System.out.println(resto);
            }
        }
    }

    public static class CalculadoraPreguicosa{
        public static void main(String[]args) {
            Scanner scanner = new Scanner(System.in);


            int produto1;
            int produto2;
            int produto3;
            int produto4;


            System.out.println(" Digite o valor do ptoduto1: ");
            produto1 = scanner.nextInt();
            System.out.println(" Digite o valor do ptoduto2: ");
            produto2 = scanner.nextInt();
            System.out.println(" Digite o valor do ptoduto3: ");
            produto3 = scanner.nextInt();
            System.out.println(" Digite o valor do ptoduto4: ");
            produto4 = scanner.nextInt();

            System.out.println(" O valor medio dos produtos e: ");
            int totalGeral = produto1 +
                    produto2 +
                    produto3 +
                    produto4;

            int mediadosprodutos  = totalGeral / 4;
            System.out.println(mediadosprodutos);
        }
    }
}
