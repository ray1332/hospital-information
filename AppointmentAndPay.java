import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 医疗信息管理系统 - 在线预约与支付模块
 */
public class MedicalAppointmentSystem {
    // 模拟数据库存储
    private static Map<Integer, Doctor> doctors = new HashMap<>();
    private static Map<Integer, Appointment> appointments = new HashMap<>();
    private static Map<String, Patient> patients = new HashMap<>();
    private static int appointmentIdCounter = 1;

    public static void main(String[] args) {
        initializeData();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== 医疗信息管理系统 ===");
            System.out.println("1. 患者登录/注册");
            System.out.println("2. 查看医生排班");
            System.out.println("3. 预约挂号");
            System.out.println("4. 取消预约");
            System.out.println("5. 支付预约费用");
            System.out.println("6. 查看我的预约");
            System.out.println("0. 退出系统");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            
            switch (choice) {
                case 1:
                    patientLoginOrRegister(scanner);
                    break;
                case 2:
                    displayDoctorSchedules();
                    break;
                case 3:
                    makeAppointment(scanner);
                    break;
                case 4:
                    cancelAppointment(scanner);
                    break;
                case 5:
                    processPayment(scanner);
                    break;
                case 6:
                    viewMyAppointments(scanner);
                    break;
                case 0:
                    System.out.println("感谢使用医疗信息管理系统，再见！");
                    return;
                default:
                    System.out.println("无效的选择，请重新输入。");
            }
        }
    }

    // 初始化测试数据
    private static void initializeData() {
        // 添加医生
        doctors.put(1, new Doctor(1, "张医生", "内科", "周一至周五 9:00-12:00, 14:00-17:00", 50.0));
        doctors.put(2, new Doctor(2, "李医生", "外科", "周二、周四、周六 8:00-12:00", 60.0));
        doctors.put(3, new Doctor(3, "王医生", "儿科", "周一至周六 13:00-18:00", 45.0));
        
        // 添加测试患者
        patients.put("patient1", new Patient("patient1", "张三", "12345678901", "zhangsan@example.com"));
        patients.put("patient2", new Patient("patient2", "李四", "12345678902", "lisi@example.com"));
    }

    // 患者登录/注册
    private static void patientLoginOrRegister(Scanner scanner) {
        System.out.print("请输入患者ID: ");
        String patientId = scanner.nextLine();
        
        if (patients.containsKey(patientId)) {
            System.out.println("登录成功！欢迎回来，" + patients.get(patientId).getName());
        } else {
            System.out.println("新患者注册");
            System.out.print("请输入姓名: ");
            String name = scanner.nextLine();
            System.out.print("请输入手机号: ");
            String phone = scanner.nextLine();
            System.out.print("请输入邮箱: ");
            String email = scanner.nextLine();
            
            Patient newPatient = new Patient(patientId, name, phone, email);
            patients.put(patientId, newPatient);
            System.out.println("注册成功！欢迎使用我们的系统，" + name);
        }
    }

    // 显示医生排班
    private static void displayDoctorSchedules() {
        System.out.println("\n=== 医生排班表 ===");
        System.out.printf("%-5s %-10s %-10s %-30s %-10s\n", "ID", "姓名", "科室", "出诊时间", "挂号费");
        for (Doctor doctor : doctors.values()) {
            System.out.printf("%-5d %-10s %-10s %-30s %-10.2f\n", 
                doctor.getId(), doctor.getName(), doctor.getDepartment(), 
                doctor.getSchedule(), doctor.getFee());
        }
    }

    // 预约挂号
    private static void makeAppointment(Scanner scanner) {
        System.out.print("请输入您的患者ID: ");
        String patientId = scanner.nextLine();
        
        if (!patients.containsKey(patientId)) {
            System.out.println("患者ID不存在，请先注册！");
            return;
        }
        
        displayDoctorSchedules();
        System.out.print("请选择医生ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        if (!doctors.containsKey(doctorId)) {
            System.out.println("无效的医生ID！");
            return;
        }
        
        System.out.print("请输入预约日期和时间(格式: yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        LocalDateTime appointmentTime = LocalDateTime.parse(dateTimeStr.replace(" ", "T"));
        
        // 检查时间冲突等业务逻辑可以在这里添加
        
        Doctor selectedDoctor = doctors.get(doctorId);
        Appointment newAppointment = new Appointment(
            appointmentIdCounter++, 
            patients.get(patientId), 
            selectedDoctor, 
            appointmentTime, 
            selectedDoctor.getFee(), 
            "待支付"
        );
        
        appointments.put(newAppointment.getId(), newAppointment);
        System.out.println("\n预约成功！");
        System.out.println("预约ID: " + newAppointment.getId());
        System.out.println("医生: " + selectedDoctor.getName());
        System.out.println("时间: " + appointmentTime);
        System.out.println("费用: " + selectedDoctor.getFee());
        System.out.println("状态: 待支付");
    }

    // 取消预约
    private static void cancelAppointment(Scanner scanner) {
        System.out.print("请输入您的患者ID: ");
        String patientId = scanner.nextLine();
        
        System.out.print("请输入要取消的预约ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        if (!appointments.containsKey(appointmentId)) {
            System.out.println("无效的预约ID！");
            return;
        }
        
        Appointment appointment = appointments.get(appointmentId);
        if (!appointment.getPatient().getId().equals(patientId)) {
            System.out.println("您只能取消自己的预约！");
            return;
        }
        
        if (appointment.getStatus().equals("已取消")) {
            System.out.println("该预约已被取消！");
            return;
        }
        
        if (appointment.getStatus().equals("已完成")) {
            System.out.println("该预约已完成，不能取消！");
            return;
        }
        
        appointment.setStatus("已取消");
        System.out.println("预约已成功取消！");
    }

    // 处理支付
    private static void processPayment(Scanner scanner) {
        System.out.print("请输入您的患者ID: ");
        String patientId = scanner.nextLine();
        
        System.out.print("请输入要支付的预约ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        if (!appointments.containsKey(appointmentId)) {
            System.out.println("无效的预约ID！");
            return;
        }
        
        Appointment appointment = appointments.get(appointmentId);
        if (!appointment.getPatient().getId().equals(patientId)) {
            System.out.println("您只能支付自己的预约！");
            return;
        }
        
        if (!appointment.getStatus().equals("待支付")) {
            System.out.println("该预约不需要支付或已支付！");
            return;
        }
        
        System.out.println("\n=== 支付信息 ===");
        System.out.println("预约ID: " + appointment.getId());
        System.out.println("医生: " + appointment.getDoctor().getName());
        System.out.println("时间: " + appointment.getAppointmentTime());
        System.out.println("金额: " + appointment.getFee());
        
        System.out.println("\n请选择支付方式:");
        System.out.println("1. 支付宝");
        System.out.println("2. 微信支付");
        System.out.println("3. 银行卡");
        System.out.print("请选择: ");
        int paymentMethod = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
        
        // 模拟支付处理
        System.out.print("请输入支付金额: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // 消耗换行符
        
        if (Math.abs(amount - appointment.getFee()) > 0.01) {
            System.out.println("支付金额与预约费用不符！");
            return;
        }
        
        // 模拟支付成功
        appointment.setStatus("已支付");
        System.out.println("支付成功！预约状态已更新。");
        
        // 发送确认通知
        sendConfirmation(appointment);
    }

    // 发送确认通知
    private static void sendConfirmation(Appointment appointment) {
        System.out.println("\n=== 预约确认通知 ===");
        System.out.println("发送至: " + appointment.getPatient().getEmail());
        System.out.println("发送至: " + appointment.getPatient().getPhone());
        System.out.println("内容: 尊敬的" + appointment.getPatient().getName() + "，您的预约已确认。");
        System.out.println("      医生: " + appointment.getDoctor().getName());
        System.out.println("      时间: " + appointment.getAppointmentTime());
        System.out.println("      地点: " + appointment.getDoctor().getDepartment() + "诊室");
    }

    // 查看我的预约
    private static void viewMyAppointments(Scanner scanner) {
        System.out.print("请输入您的患者ID: ");
        String patientId = scanner.nextLine();
        
        System.out.println("\n=== 我的预约 ===");
        boolean hasAppointments = false;
        
        for (Appointment appointment : appointments.values()) {
            if (appointment.getPatient().getId().equals(patientId)) {
                System.out.println("预约ID: " + appointment.getId());
                System.out.println("医生: " + appointment.getDoctor().getName());
                System.out.println("科室: " + appointment.getDoctor().getDepartment());
                System.out.println("时间: " + appointment.getAppointmentTime());
                System.out.println("费用: " + appointment.getFee());
                System.out.println("状态: " + appointment.getStatus());
                System.out.println("-----------------------");
                hasAppointments = true;
            }
        }
        
        if (!hasAppointments) {
            System.out.println("您目前没有预约记录。");
        }
    }
}

// 医生类
class Doctor {
    private int id;
    private String name;
    private String department;
    private String schedule;
    private double fee;
    
    public Doctor(int id, String name, String department, String schedule, double fee) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.schedule = schedule;
        this.fee = fee;
    }
    
    // Getter方法
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getSchedule() { return schedule; }
    public double getFee() { return fee; }
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

// 预约类
class Appointment {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentTime;
    private double fee;
    private String status;
    
    public Appointment(int id, Patient patient, Doctor doctor, LocalDateTime appointmentTime, double fee, String status) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = appointmentTime;
        this.fee = fee;
        this.status = status;
    }
    
    // Getter和Setter方法
    public int getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public double getFee() { return fee; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
