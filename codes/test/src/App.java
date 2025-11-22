import java.util.Scanner;

// public class App {
//     public static void main(String[] args) throws Exception {

//         int[] list = {1, 2, 3, 5, 4}; 
        
//         for (int i = 0, j = list.length - 1; i < list.length/2; i++, j--)         { 
//             // Swap list[i] with list[j] 
//             int temp = list[i]; 
//             list[i] = list[j]; 
//             list[j] = temp; 
//         }
            
//             for(int i  : list)
//                 System.out.println(i + "  ");

//     }
// }


public class App { public static void main(String[] args) { printMax(34, 3, 3, 2, 22); printMax(new double[]{1, 2, 3}); } public static void printMax(double... numbers) { if (numbers.length == 0) { System.out.println("No argument passed"); return; } double result = numbers[0]; for (int i = 1; i < numbers.length; i++) if (numbers[i] > result) result = numbers[i]; System.out.println("The max value is " + result); } }