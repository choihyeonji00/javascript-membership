package ksnu.a2101029;

import java.io.*;
import java.util.*;

class Member implements Comparable<Member> {
   private String id;
   private String name;
   private String password;

   public Member(String id, String name, String password) {
      this.id = id;
      this.name = name;
      this.password = password;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getPassword() {
      return password;
   }

   public int compareTo(Member other) {
      return name.compareTo(other.getName());
   }
}

public class MembershipProgram {
   private static final String FILE_PATH = "subscribers.dat";
   private static final String ROOT_ID = "root";
   private static final String ROOT_PASSWORD = "1234";

   private static Scanner scanner;
   private static List<Member> members;

   public static void main(String[] args) {
      scanner = new Scanner(System.in);
      members = new ArrayList<>();

      loadMembersFromFile();

      int choice;
      do {
         printMenu();
         choice = readChoice();
         switch (choice) {
         case 1:
            login();
            break;
         case 2:
            register();
            break;
         case 3:
            displayMembers();
            break;
         case 4:
            System.out.println("프로그램을 종료합니다.");
            break;
         default:
            System.out.println("잘못된 선택입니다. 다시 입력해주세요.");
         }
      } while (choice != 4);

      saveMembersToFile();
      scanner.close();
   }

   private static void printMenu() {
      System.out.println("1. 로그인");
      System.out.println("2. 신규회원가입");
      System.out.println("3. 회원정보보기");
      System.out.println("4. 종료");
   }

   private static int readChoice() {
      System.out.print("선택: ");
      return scanner.nextInt();
   }

   private static void login() {
      System.out.print("id: ");
      String id = scanner.next();
      System.out.print("passwd: ");
      String password = scanner.next();


      if(members.isEmpty()) {
         if (id.equals(ROOT_ID) && password.equals(ROOT_PASSWORD)) {
            System.out.println(id + " 님 반갑습니다.");
            Member root = new Member(ROOT_ID, "root", ROOT_PASSWORD);
            members.add(root);
            saveMembersToFile();
         }else {
            System.out.println("일치하는 회원 정보를 찾을 수 없습니다.");
         }
      } else {
         boolean found = false;
         for (Member member : members) {
            if (member.getId().equals(id) && member.getPassword().equals(password)) {
               System.out.println(member.getName() + " 님 반갑습니다.");
               found = true;
               break;
            }
         }

         if (!found) {
            System.out.println("일치하는 회원 정보를 찾을 수 없습니다.");
         }
      }
   }

   private static void register() {
      System.out.print("- 세부사항입력 : ");
      String id = scanner.next();
      String name = scanner.next();
      String password = scanner.next();

      Member member = new Member(id, name, password);
      members.add(member);
      saveMembersToFile();
      System.out.println("신규 회원가입이 완료되었습니다.");
   }

   private static void displayMembers() {
      Collections.sort(members);

      System.out.println("- 번호\tid\t이름\t패스워드");
      for (int i = 0; i < members.size(); i++) {
         Member member = members.get(i);
         System.out.println((i + 1) + "\t" + member.getId() + "\t" + member.getName() + "\t" + member.getPassword());
      }
   }

   private static void loadMembersFromFile() {
      try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
         String line;
         while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            String id = data[0].trim();
            String name = data[1].trim();
            String password = data[2].trim();
            Member member = new Member(id, name, password);
            members.add(member);
         }
      } catch (FileNotFoundException e) {
         System.out.println("파일이 존재하지 않습니다.");
      } catch (IOException e) {
         System.out.println("파일을 읽어오는 도중 오류가 발생했습니다.");
         e.printStackTrace();
      }
   }

   private static void saveMembersToFile() {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
         for (Member member : members) {
            writer.write(member.getId() + ", " + member.getName() + ", " + member.getPassword());
            writer.newLine();
         }
      } catch (IOException e) {
         System.out.println("파일에 회원 정보를 저장하는 도중 오류가 발생했습니다.");
         e.printStackTrace();
      }
   }
}