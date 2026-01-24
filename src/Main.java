import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<Book> listBook = new ArrayList<>();
        Scanner x = new Scanner(System.in);
        String msg = """
            Chương trình quản lý sách
                1. Thêm 1 cuốn sách
                2. Xóa 1 cuốn sách
                3. Thay đổi sách
                4. Xuất thông tin
                5. Tìm sách Lập trình
                6. Lấy sách tối đa theo giá
                7. Tìm kiếm theo tác giả
                0. Thoát
            Chọn chức năng:""";

        int chon = 0;
        do
        {
            System.out.printf(msg);
            chon = x.nextInt();
            switch (chon)
            {
                case 1 -> {
                    Book newBook = new Book();
                    newBook.input();
                    listBook.add(newBook);
                }
                case 2 -> {
                    System.out.print("Nhập vào mã sách cần xóa:");
                    int bookid = x.nextInt();
                    // kiểm tra mã sách
                    Book find = listBook.stream().filter(p -> p.getId() == bookid).findFirst().orElseThrow();
                    listBook.remove(find);
                    System.out.print("Đã xóa sách thành công");
                }
                case 3 -> {
                    System.out.print("Nhập vào mã sách cần điều chỉnh: ");
                    int bookid = x.nextInt();
                    try {
                        // Tìm cuốn sách có ID khớp trong danh sách
                        Book find = listBook.stream()
                                .filter(p -> p.getId() == bookid)
                                .findFirst()
                                .orElseThrow();

                        // Gọi lại phương thức nhập để ghi đè thông tin mới
                        System.out.println("Nhập lại thông tin mới cho sách:");
                        find.input();
                        System.out.println("Cập nhật thông tin thành công!");
                    } catch (Exception e) {
                        System.out.println("Không tìm thấy mã sách này!");
                    }
                }
                case 4 -> {
                    System.out.println("\n Xuất thông tin danh sách ");
                    listBook.forEach(p -> p.output());
                }
                case 5 -> {
                    List<Book> list5 = listBook.stream()
                            .filter(u -> u.getTitle().toLowerCase().contains("lập trình"))
                            .toList();
                    list5.forEach(Book::output);
                }
                case 6 -> {
                    System.out.print("Nhập mức giá tối đa: ");
                    long maxPrice = x.nextLong();
                    System.out.print("Nhập số lượng sách tối đa muốn hiển thị: ");
                    int maxLimit = x.nextInt();

                    List<Book> list6 = listBook.stream()
                            .filter(b -> b.getPrice() <= maxPrice) // Lọc theo giá
                            .limit(maxLimit)                      // Giới hạn số lượng
                            .toList();

                    System.out.println("\n Kết quả lọc theo giá: ");
                    list6.forEach(Book::output);
                }
                case 7 -> {
                    x.nextLine(); // Xóa bộ nhớ đệm
                    System.out.print("Nhập danh sách tác giả (phân cách bằng dấu phẩy): ");
                    String inputAuthors = x.nextLine();

                    // Chuyển tập hợp tác giả cần tìm sang tập Set
                    Set<String> authorSet = Arrays.stream(inputAuthors.split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet());

                    // Filter tác giả nằm trong tập Set
                    List<Book> list7 = listBook.stream()
                            .filter(b -> authorSet.contains(b.getAuthor()))
                            .toList();

                    System.out.println("\n Kết quả tìm theo nhóm tác giả: ");
                    list7.forEach(Book::output);
                }
            }
        } while (chon != 0);
    }
    }
