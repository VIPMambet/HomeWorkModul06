import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Паттерн "Одиночка"
class ConfigurationManager {
    private static ConfigurationManager instance;
    private Map<String, String> settings;

    // Приватный конструктор для предотвращения создания объектов
    private ConfigurationManager() {
        settings = new HashMap<>();
    }

    // Метод для получения единственного экземпляра
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    // Метод для загрузки настроек
    public void loadSettings() {
        // Здесь можно загрузить настройки из файла
        settings.put("theme", "dark");
        settings.put("language", "en");
        System.out.println("Настройки загружены.");
    }

    // Метод для чтения настройки
    public String getSetting(String key) {
        return settings.get(key);
    }

    // Метод для изменения настройки
    public void setSetting(String key, String value) {
        settings.put(key, value);
    }
}

// Паттерн "Строитель"
interface IReportBuilder {
    void setHeader(String header);
    void setContent(String content);
    void setFooter(String footer);
    Report getReport();
}

class TextReportBuilder implements IReportBuilder {
    private Report report;

    public TextReportBuilder() {
        report = new Report();
    }

    @Override
    public void setHeader(String header) {
        report.setHeader(header);
    }

    @Override
    public void setContent(String content) {
        report.setContent(content);
    }

    @Override
    public void setFooter(String footer) {
        report.setFooter(footer);
    }

    @Override
    public Report getReport() {
        return report;
    }
}

class HtmlReportBuilder implements IReportBuilder {
    private Report report;

    public HtmlReportBuilder() {
        report = new Report();
    }

    @Override
    public void setHeader(String header) {
        report.setHeader("<h1>" + header + "</h1>");
    }

    @Override
    public void setContent(String content) {
        report.setContent("<p>" + content + "</p>");
    }

    @Override
    public void setFooter(String footer) {
        report.setFooter("<footer>" + footer + "</footer>");
    }

    @Override
    public Report getReport() {
        return report;
    }
}

class Report {
    private String header;
    private String content;
    private String footer;

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        return "Report:\n" + header + "\n" + content + "\n" + footer;
    }
}

class ReportDirector {
    public void constructReport(IReportBuilder builder) {
        builder.setHeader("Заголовок отчета");
        builder.setContent("Содержимое отчета");
        builder.setFooter("Подвал отчета");
    }
}

// Паттерн "Прототип"
class Product implements Cloneable {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    protected Product clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Клонирование не удалось", e);
        }
    }

    @Override
    public String toString() {
        return name + ": $" + price;
    }
}

class Order implements Cloneable {
    private List<Product> products;
    private double deliveryCost;
    private double discount;

    public Order() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void setDeliveryCost(double cost) {
        this.deliveryCost = cost;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    protected Order clone() {
        Order cloned = new Order();
        for (Product product : products) {
            cloned.addProduct(product.clone());
        }
        cloned.setDeliveryCost(this.deliveryCost);
        cloned.setDiscount(this.discount);
        return cloned;
    }

    @Override
    public String toString() {
        return "Order:\n" + products + "\nDelivery Cost: $" + deliveryCost + "\nDiscount: $" + discount;
    }
}

// Основной класс для тестирования
public class Main {
    public static void main(String[] args) {
        // Тестирование ConfigurationManager
        ConfigurationManager config = ConfigurationManager.getInstance();
        config.loadSettings();
        System.out.println("Тема: " + config.getSetting("theme"));
        System.out.println("Язык: " + config.getSetting("language"));

        // Проверка, что все вызовы возвращают один и тот же экземпляр
        ConfigurationManager anotherConfig = ConfigurationManager.getInstance();
        System.out.println("Одинаковы? " + (config == anotherConfig));

        // Тестирование Builder
        ReportDirector director = new ReportDirector();

        // Создание текстового отчета
        IReportBuilder textBuilder = new TextReportBuilder();
        director.constructReport(textBuilder);
        Report textReport = textBuilder.getReport();
        System.out.println(textReport);

        // Создание HTML отчета
        IReportBuilder htmlBuilder = new HtmlReportBuilder();
        director.constructReport(htmlBuilder);
        Report htmlReport = htmlBuilder.getReport();
        System.out.println(htmlReport);

        // Тестирование Prototype
        Order order1 = new Order();
        order1.addProduct(new Product("Товар 1", 10.0));
        order1.addProduct(new Product("Товар 2", 15.0));
        order1.setDeliveryCost(5.0);
        order1.setDiscount(2.0);

        Order order2 = order1.clone(); // Клонирование заказа
        order2.setDiscount(3.0); // Изменяем только скидку

        System.out.println(order1);
        System.out.println(order2);
    }
}
