import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 电子病历管理系统
 */
public class ElectronicMedicalRecordSystem {
    // 模拟数据库存储
    private static Map<String, Patient> patients = new HashMap<>();
    private static Map<Integer, Doctor> doctors = new HashMap<>();
    private static Map<Integer, MedicalRecord> medicalRecords = new HashMap<>();
    private static int recordIdCounter = 1;
    
    public static void main(String[] args) {
        initializeData();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== 电子病历管理系统 ===");
            System.out.println("1. 医生登录");
            System.out.println("2. 患者登录");
            System.out.println("3. 退出系统");
            System.out.print("请选择身份: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            
            switch (choice) {
                case 1:
                    doctorMenu(scanner);
                    break;
                case 2:
                    patientMenu(scanner);
                    break;
                case 3:
                    System.out.println("感谢使用电子病历管理系统，再见！");
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    // 初始化测试数据
    private static void initializeData() {
        // 添加患者
        patients.put("p001", new Patient("p001", "张三", "13800138001", "zhangsan@example.com"));
        patients.put("p002", new Patient("p002", "李四", "13800138002", "lisi@example.com"));
        
        // 添加医生
        doctors.put(101, new Doctor(101, "王医生", "内科"));
        doctors.put(102, new Doctor(102, "李医生", "外科"));
        
        // 添加测试病历
        MedicalRecord record1 = new MedicalRecord(
            recordIdCounter++, 
            patients.get("p001"), 
            doctors.get(101), 
            LocalDate.now().minusDays(10), 
            "上呼吸道感染", 
            "发热、咳嗽、咽痛", 
            "血常规检查", 
            "阿莫西林 0.5g 每日三次，连用7天", 
            "多喝水，注意休息", 
            "内科"
        );
        medicalRecords.put(record1.getId(), record1);
        
        MedicalRecord record2 = new MedicalRecord(
            recordIdCounter++, 
            patients.get("p001"), 
            doctors.get(102), 
            LocalDate.now().minusDays(5), 
            "急性阑尾炎", 
            "右下腹疼痛", 
            "腹部超声检查", 
            "头孢曲松钠 2g 每日一次", 
            "建议手术治疗", 
            "外科"
        );
        medicalRecords.put(record2.getId(), record2);
    }

    // 医生菜单
    private static void doctorMenu(Scanner scanner) {
        System.out.print("请输入医生ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        if (!doctors.containsKey(doctorId)) {
            System.out.println("医生ID不存在！");
            return;
        }
        
        Doctor currentDoctor = doctors.get(doctorId);
        System.out.println("欢迎，" + currentDoctor.getName() + "医生！");
        
        while (true) {
            System.out.println("\n=== 医生工作台 ===");
            System.out.println("1. 创建新病历");
            System.out.println("2. 修改病历");
            System.out.println("3. 查看患者病历");
            System.out.println("4. 返回上级菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            
            switch (choice) {
                case 1:
                    createMedicalRecord(scanner, currentDoctor);
                    break;
                case 2:
                    updateMedicalRecord(scanner, currentDoctor);
                    break;
                case 3:
                    viewPatientRecords(scanner, currentDoctor);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    // 创建病历
    private static void createMedicalRecord(Scanner scanner, Doctor doctor) {
        System.out.print("请输入患者ID: ");
        String patientId = scanner.nextLine();
        
        if (!patients.containsKey(patientId)) {
            System.out.println("患者ID不存在！");
            return;
        }
        
        Patient patient = patients.get(patientId);
        
        System.out.print("就诊日期(格式: yyyy-MM-dd): ");
        LocalDate visitDate = LocalDate.parse(scanner.nextLine());
        
        System.out.print("初步诊断: ");
        String diagnosis = scanner.nextLine();
        
        System.out.print("症状描述: ");
        String symptoms = scanner.nextLine();
        
        System.out.print("检查项目: ");
        String examinations = scanner.nextLine();
        
        System.out.print("治疗方案: ");
        String treatment = scanner.nextLine();
        
        System.out.print("医嘱: ");
        String medicalAdvice = scanner.nextLine();
        
        System.out.print("科室: ");
        String department = scanner.nextLine();
        
        MedicalRecord newRecord = new MedicalRecord(
            recordIdCounter++, 
            patient, 
            doctor, 
            visitDate, 
            diagnosis, 
            symptoms, 
            examinations, 
            treatment, 
            medicalAdvice, 
            department
        );
        
        medicalRecords.put(newRecord.getId(), newRecord);
        System.out.println("病历创建成功！ID: " + newRecord.getId());
    }

    // 修改病历
    private static void updateMedicalRecord(Scanner scanner, Doctor doctor) {
        System.out.print("请输入要修改的病历ID: ");
        int recordId = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        if (!medicalRecords.containsKey(recordId)) {
            System.out.println("病历ID不存在！");
            return;
        }
        
        MedicalRecord record = medicalRecords.get(recordId);
        
        // 检查医生是否有权限修改该病历
        if (record.getDoctor().getId() != doctor.getId()) {
            System.out.println("您只能修改自己创建的病历！");
            return;
        }
        
        System.out.println("\n当前病历信息:");
        displayRecordDetails(record);
        
        System.out.println("\n请选择要修改的内容:");
        System.out.println("1. 诊断结果");
        System.out.println("2. 症状描述");
        System.out.println("3. 检查项目");
        System.out.println("4. 治疗方案");
        System.out.println("5. 医嘱");
        System.out.println("0. 取消修改");
        System.out.print("请选择: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        switch (choice) {
            case 1:
                System.out.print("输入新的诊断结果: ");
                record.setDiagnosis(scanner.nextLine());
                break;
            case 2:
                System.out.print("输入新的症状描述: ");
                record.setSymptoms(scanner.nextLine());
                break;
            case 3:
                System.out.print("输入新的检查项目: ");
                record.setExaminations(scanner.nextLine());
                break;
            case 4:
                System.out.print("输入新的治疗方案: ");
                record.setTreatment(scanner.nextLine());
                break;
            case 5:
                System.out.print("输入新的医嘱: ");
                record.setMedicalAdvice(scanner.nextLine());
                break;
            case 0:
                return;
            default:
                System.out.println("无效的选择！");
                return;
        }
        
        record.setLastUpdated(LocalDateTime.now());
        System.out.println("病历更新成功！");
    }

    // 查看患者病历
    private static void viewPatientRecords(Scanner scanner, Doctor doctor) {
        System.out.print("请输入患者ID: ");
        String patientId = scanner.nextLine();
        
        if (!patients.containsKey(patientId)) {
            System.out.println("患者ID不存在！");
            return;
        }
        
        Patient patient = patients.get(patientId);
        
        System.out.println("\n=== " + patient.getName() + "的病历记录 ===");
        System.out.println("1. 按时间排序");
        System.out.println("2. 按科室筛选");
        System.out.println("3. 按病种筛选");
        System.out.println("4. 查看全部");
        System.out.print("请选择查看方式: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        List<MedicalRecord> records = medicalRecords.values().stream()
            .filter(r -> r.getPatient().getId().equals(patientId))
            .collect(Collectors.toList());
        
        if (records.isEmpty()) {
            System.out.println("该患者没有病历记录。");
            return;
        }
        
        switch (choice) {
            case 1:
                records.sort((r1, r2) -> r2.getVisitDate().compareTo(r1.getVisitDate()));
                break;
            case 2:
                System.out.print("请输入科室名称: ");
                String department = scanner.nextLine();
                records = records.stream()
                    .filter(r -> r.getDepartment().equalsIgnoreCase(department))
                    .collect(Collectors.toList());
                break;
            case 3:
                System.out.print("请输入病种关键词: ");
                String keyword = scanner.nextLine();
                records = records.stream()
                    .filter(r -> r.getDiagnosis().contains(keyword))
                    .collect(Collectors.toList());
                break;
            case 4:
                break;
            default:
                System.out.println("无效的选择，显示全部记录。");
        }
        
        if (records.isEmpty()) {
            System.out.println("没有找到符合条件的病历记录。");
            return;
        }
        
        System.out.println("\n=== 病历列表 ===");
        System.out.printf("%-8s %-12s %-10s %-20s %-15s\n", "病历ID", "就诊日期", "科室", "诊断", "医生");
        for (MedicalRecord record : records) {
            System.out.printf("%-8d %-12s %-10s %-20s %-15s\n", 
                record.getId(), 
                record.getVisitDate(), 
                record.getDepartment(), 
                record.getDiagnosis(), 
                record.getDoctor().getName());
        }
        
        System.out.print("\n输入病历ID查看详情(0返回): ");
        int recordId = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        if (recordId != 0 && medicalRecords.containsKey(recordId)) {
            displayRecordDetails(medicalRecords.get(recordId));
        }
    }

    // 患者菜单
    private static void patientMenu(Scanner scanner) {
        System.out.print("请输入患者ID: ");
        String patientId = scanner.nextLine();
        
        if (!patients.containsKey(patientId)) {
            System.out.println("患者ID不存在！");
            return;
        }
        
        Patient currentPatient = patients.get(patientId);
        System.out.println("欢迎，" + currentPatient.getName() + "！");
        
        while (true) {
            System.out.println("\n=== 患者工作台 ===");
            System.out.println("1. 查看我的病历");
            System.out.println("2. 返回上级菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            
            switch (choice) {
                case 1:
                    viewMyRecords(currentPatient, scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    // 患者查看自己的病历
    private static void viewMyRecords(Patient patient, Scanner scanner) {
        List<MedicalRecord> records = medicalRecords.values().stream()
            .filter(r -> r.getPatient().getId().equals(patient.getId()))
            .collect(Collectors.toList());
        
        if (records.isEmpty()) {
            System.out.println("您目前没有病历记录。");
            return;
        }
        
        System.out.println("\n=== 我的病历记录 ===");
        System.out.println("1. 按时间排序");
        System.out.println("2. 按科室筛选");
        System.out.println("3. 按病种筛选");
        System.out.println("4. 查看全部");
        System.out.print("请选择查看方式: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        switch (choice) {
            case 1:
                records.sort((r1, r2) -> r2.getVisitDate().compareTo(r1.getVisitDate()));
                break;
            case 2:
                System.out.print("请输入科室名称: ");
                String department = scanner.nextLine();
                records = records.stream()
                    .filter(r -> r.getDepartment().equalsIgnoreCase(department))
                    .collect(Collectors.toList());
                break;
            case 3:
                System.out.print("请输入病种关键词: ");
                String keyword = scanner.nextLine();
                records = records.stream()
                    .filter(r -> r.getDiagnosis().contains(keyword))
                    .collect(Collectors.toList());
                break;
            case 4:
                break;
            default:
                System.out.println("无效的选择，显示全部记录。");
        }
        
        if (records.isEmpty()) {
            System.out.println("没有找到符合条件的病历记录。");
            return;
        }
        
        System.out.println("\n=== 病历列表 ===");
        System.out.printf("%-8s %-12s %-10s %-20s %-15s\n", "病历ID", "就诊日期", "科室", "诊断", "医生");
        for (MedicalRecord record : records) {
            System.out.printf("%-8d %-12s %-10s %-20s %-15s\n", 
                record.getId(), 
                record.getVisitDate(), 
                record.getDepartment(), 
                record.getDiagnosis(), 
                record.getDoctor().getName());
        }
        
        System.out.print("\n输入病历ID查看详情(0返回): ");
        int recordId = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        if (recordId != 0 && medicalRecords.containsKey(recordId)) {
            displayRecordDetails(medicalRecords.get(recordId));
        }
    }

    // 显示病历详情
    private static void displayRecordDetails(MedicalRecord record) {
        System.out.println("\n=== 病历详情 ===");
        System.out.println("病历ID: " + record.getId());
        System.out.println("患者: " + record.getPatient().getName());
        System.out.println("医生: " + record.getDoctor().getName() + " (" + record.getDepartment() + ")");
        System.out.println("就诊日期: " + record.getVisitDate());
        System.out.println("最后更新时间: " + record.getLastUpdated());
        System.out.println("\n诊断结果: " + record.getDiagnosis());
        System.out.println("症状描述: " + record.getSymptoms());
        System.out.println("检查项目: " + record.getExaminations());
        System.out.println("治疗方案: " + record.getTreatment());
        System.out.println("医嘱: " + record.getMedicalAdvice());
    }
}

// 患者类
class Patient {
    private String id;
    private String name;
    private String phone;
    private String email;
    
    public Patient(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    
    // Getter方法
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}

// 医生类
class Doctor {
    private int id;
    private String name;
    private String department;
    
    public Doctor(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }
    
    // Getter方法
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
}

// 病历类
class MedicalRecord {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private LocalDate visitDate;
    private LocalDateTime lastUpdated;
    private String diagnosis;
    private String symptoms;
    private String examinations;
    private String treatment;
    private String medicalAdvice;
    private String department;
    
    public MedicalRecord(int id, Patient patient, Doctor doctor, LocalDate visitDate, 
                        String diagnosis, String symptoms, String examinations, 
                        String treatment, String medicalAdvice, String department) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.visitDate = visitDate;
        this.lastUpdated = LocalDateTime.now();
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.examinations = examinations;
        this.treatment = treatment;
        this.medicalAdvice = medicalAdvice;
        this.department = department;
    }
    
    // Getter和Setter方法
    public int getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDate getVisitDate() { return visitDate; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public String getDiagnosis() { return diagnosis; }
    public String getSymptoms() { return symptoms; }
    public String getExaminations() { return examinations; }
    public String getTreatment() { return treatment; }
    public String getMedicalAdvice() { return medicalAdvice; }
    public String getDepartment() { return department; }
    
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setExaminations(String examinations) { this.examinations = examinations; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public void setMedicalAdvice(String medicalAdvice) { this.medicalAdvice = medicalAdvice; }
}
