import java.util.*;

// 药品或设备类
class MedicalItem {
    private String id;
    private String name;
    private String category;
    private String supplier;
    private Date expirationDate;
    private int stock;

    public MedicalItem(String id, String name, String category, String supplier, Date expirationDate, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.supplier = supplier;
        this.expirationDate = expirationDate;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSupplier() {
        return supplier;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", 名称: " + name +
                ", 类别: " + category +
                ", 供应商: " + supplier +
                ", 有效期: " + expirationDate +
                ", 库存量: " + stock;
    }
}

// 智能库存管理类
public class MedicalInventoryManager {
    private List<MedicalItem> items = new ArrayList<>();
    private Map<String, Integer> stockThresholds = new HashMap<>();

    // 录入药品或设备信息
    public void addItem(MedicalItem item) {
        items.add(item);
        checkStockAlarm(item);
    }

    // 查询药品或设备信息
    public List<MedicalItem> queryItems(String category, String name, String supplier) {
        List<MedicalItem> result = new ArrayList<>();
        for (MedicalItem item : items) {
            if ((category == null || item.getCategory().equals(category)) &&
                    (name == null || item.getName().equals(name)) &&
                    (supplier == null || item.getSupplier().equals(supplier))) {
                result.add(item);
            }
        }
        return result;
    }

    // 更新药品或设备信息
    public void updateItem(String id, int newStock) {
        for (MedicalItem item : items) {
            if (item.getId().equals(id)) {
                item.setStock(newStock);
                checkStockAlarm(item);
                break;
            }
        }
    }

    // 删除药品或设备信息
    public void deleteItem(String id) {
        Iterator<MedicalItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            MedicalItem item = iterator.next();
            if (item.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
    }

    // 设置库存阈值
    public void setStockThreshold(String id, int threshold) {
        stockThresholds.put(id, threshold);
    }

    // 检查库存预警
    private void checkStockAlarm(MedicalItem item) {
        if (stockThresholds.containsKey(item.getId())) {
            int threshold = stockThresholds.get(item.getId());
            if (item.getStock() < threshold) {
                System.out.println("[库存预警] " + item.getName() + " 库存量低于阈值: " + item.getStock());
                sendAlarmNotification(item);
            }
        }
    }

    // 发送预警通知
    private void sendAlarmNotification(MedicalItem item) {
        System.out.println("[通知] 已向相关人员发送 " + item.getName() + " 库存不足的通知");
    }

    // 库存数据分析，简单模拟生成预测报告
    public void generateInventoryReport() {
        System.out.println("生成库存需求预测报告...");
        // 这里可以集成机器学习模型进行预测
        System.out.println("报告生成完成");
    }

    public static void main(String[] args) {
        MedicalInventoryManager manager = new MedicalInventoryManager();

        // 模拟录入数据
        Date expirationDate = new Date();
        manager.addItem(new MedicalItem("M001", "退烧药", "药品", "供应商A", expirationDate, 50));
        manager.addItem(new MedicalItem("E001", "血压计", "设备", "供应商B", expirationDate, 20));

        // 设置库存阈值
        manager.setStockThreshold("M001", 30);
        manager.setStockThreshold("E001", 10);

        // 模拟更新库存
        manager.updateItem("M001", 20);

        // 查询药品信息
        List<MedicalItem> result = manager.queryItems("药品", null, null);
        for (MedicalItem item : result) {
            System.out.println(item);
        }

        // 生成库存报告
        manager.generateInventoryReport();
    }
}
