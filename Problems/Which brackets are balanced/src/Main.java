import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        Deque<Character> stack = new ArrayDeque<>();
        String string = scanner.nextLine();
        boolean isBalanced = true;
        for(int i = 0; i < string.length() && isBalanced; i++) {
            char c = string.charAt(i);
            if (c == '(' || c == '[' || c == '{')
                stack.push(c);
            else {
                if (stack.isEmpty()) {
                    isBalanced = false;
                } else {
                    char head = stack.pop();
                    switch (c) {
                        case ')': isBalanced = head == '('; break;
                        case ']': isBalanced = head == '['; break;
                        case '}': isBalanced = head == '{'; break;
                    }
                }
            }
        }
        System.out.println(isBalanced && stack.isEmpty());
    }
}