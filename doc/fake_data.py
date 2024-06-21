from faker import Faker
import re
import datetime
import sqlite3

# 设置中文
faker = Faker('zh_CN')
# 连接数据库
conn = sqlite3.connect('D:\\idea_worksapce\\spring-aop-demo\\spring_aop.db')



# 定义年龄计算的函数
def calc_age(birth_date):
    today = datetime.date.today()
    birth_date = datetime.datetime.strptime(birth_date,'%Y%m%d').date()
    # 计算年龄
    age = today.year - birth_date.year - ((today.month,today.day) < (birth_date.month,birth_date.day))
    
    return age

# 定义日期、性别提取函数
def extract_info_from_id(id_number):
    # 假设身份证号码为18位
    if len(id_number) != 18:
        return None, None, None
    
    # 提取出生日期（6位）
    birth_date = id_number[6:14]
    
    # 提取性别（第17位）
    sex_code = id_number[16:17]
    sex = 'F' if int(sex_code) % 2 == 0 else 'M'
 
    age = calc_age(birth_date)

    return birth_date, sex , age



# 开启数据库连接
cursor = conn.cursor()

# 如果存在表就先删除
cursor.execute('''
DROP TABLE IF EXISTS employee;
               ''')

# 创建表
cursor.execute('''
CREATE TABLE IF NOT EXISTS employee (
    id INTEGER PRIMARY KEY NOT NULL,
    name            CHAR(32),
    gender          CHAR(1),
    phone           CHAR(32),
    email           CHAR(50),
    age             INT,
    ssn             CHAR(50),
    salary          REAL,
    address         TEXT,
    create_time     DATETIME,
    update_time     DATETIME  
);
''')

# 生成并插入数据
for _ in range(50):  # 生成50条数据
    name = faker.name()
    phone = faker.phone_number()
    email = faker.email()
    ssn = faker.ssn(min_age=24,max_age=45)
    birth_date, sex ,age = extract_info_from_id(ssn)
    salary = faker.random_number(5)
    address = faker.address()
    curr_time = datetime.datetime.now()
        
    cursor.execute('''
        INSERT INTO employee (name, gender, phone, email, age, ssn, salary, address, create_time, update_time)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    ''', (name, sex, phone, email, age, ssn, salary, address, curr_time, curr_time))

# 提交事务
conn.commit()
 
# 关闭连接
cursor.close()
conn.close()

    
  