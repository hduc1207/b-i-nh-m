USE [PetHotelDB];
GO

IF OBJECT_ID('dbo.BookingServiceDetails', 'U') IS NOT NULL DROP TABLE dbo.BookingServiceDetails;
IF OBJECT_ID('dbo.ServiceDetails', 'U') IS NOT NULL DROP TABLE dbo.ServiceDetails; -- Xóa tên cũ nếu còn
IF OBJECT_ID('dbo.BookingServices', 'U') IS NOT NULL DROP TABLE dbo.BookingServices;
IF OBJECT_ID('dbo.Bills', 'U') IS NOT NULL DROP TABLE dbo.Bills;
IF OBJECT_ID('dbo.Bookings', 'U') IS NOT NULL DROP TABLE dbo.Bookings;
IF OBJECT_ID('dbo.Pets', 'U') IS NOT NULL DROP TABLE dbo.Pets;
IF OBJECT_ID('dbo.Customers', 'U') IS NOT NULL DROP TABLE dbo.Customers;
IF OBJECT_ID('dbo.Cages', 'U') IS NOT NULL DROP TABLE dbo.Cages;
IF OBJECT_ID('dbo.Services', 'U') IS NOT NULL DROP TABLE dbo.Services;
IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL DROP TABLE dbo.Users;
CREATE TABLE dbo.Users (
                           UserId INT IDENTITY(1,1) PRIMARY KEY,
                           Username VARCHAR(50) NOT NULL UNIQUE,
                           Password VARCHAR(100) NOT NULL, -- Mật khẩu (Nên mã hóa nếu có thể)
                           FullName NVARCHAR(100),
                           Role VARCHAR(20)
);

-- 2. Bảng SERVICES (Dịch vụ)
CREATE TABLE dbo.Services (
                              ServiceId INT IDENTITY(1,1) PRIMARY KEY,
                              ServiceName NVARCHAR(100),
                              Price DECIMAL(18,2),
                              Unit NVARCHAR(20) -- Lần, Giờ, Kg...
);

-- 3. Bảng CAGES (Chuồng)
CREATE TABLE dbo.Cages (
                           CageId INT IDENTITY(1,1) PRIMARY KEY,
                           CageName NVARCHAR(50),
                           Type NVARCHAR(50),      -- VIP, Thường
                           PricePerDay DECIMAL(18,2),
                           Status NVARCHAR(50)     -- 'Trống', 'Đang ở', 'Bảo trì'
);

-- 4. Bảng CUSTOMERS (Khách hàng)
CREATE TABLE dbo.Customers (
                               CustomerId INT IDENTITY(1,1) PRIMARY KEY,
    -- Tự động sinh mã KH001, KH002...
                               CustomerCode AS ('KH' + RIGHT('000' + CAST(CustomerId AS VARCHAR(10)), 3)),
                               FullName NVARCHAR(100),
                               PhoneNumber VARCHAR(20),
                               Email VARCHAR(100),
                               Address NVARCHAR(200),
                               Note NVARCHAR(MAX)
);

-- 5. Bảng PETS (Thú cưng)
CREATE TABLE dbo.Pets (
                          PetId INT IDENTITY(1,1) PRIMARY KEY,
    -- Tự động sinh mã TC001, TC002...
                          PetCode AS ('TC' + RIGHT('000' + CAST(PetId AS VARCHAR(10)), 3)),
                          PetName NVARCHAR(100),
                          Species NVARCHAR(50), -- Chó, Mèo
                          Breed NVARCHAR(100),  -- Giống (Poodle, Anh lông ngắn...)
                          Weight FLOAT,
                          HealthStatus NVARCHAR(100),
                          CustomerId INT NOT NULL,

                          CONSTRAINT FK_Pets_Customers FOREIGN KEY (CustomerId) REFERENCES dbo.Customers(CustomerId)
);

-- 6. Bảng BOOKINGS (Đặt phòng)
CREATE TABLE dbo.Bookings (
                              BookingId INT IDENTITY(1,1) PRIMARY KEY,
                              CustomerId INT,
                              PetId INT,
                              CageId INT,
                              CheckInDate DATETIME,
                              CheckOutDate DATETIME,
                              Status NVARCHAR(50),        -- 'Pending', 'Confirmed', 'Checked-in', 'Checked-out'
                              PaymentStatus NVARCHAR(50), -- 'Unpaid', 'Paid'
                              TotalPrice DECIMAL(18,2),
                              CreatedDate DATETIME DEFAULT GETDATE(),

                              CONSTRAINT FK_Bookings_Customers FOREIGN KEY (CustomerId) REFERENCES dbo.Customers(CustomerId),
                              CONSTRAINT FK_Bookings_Pets FOREIGN KEY (PetId) REFERENCES dbo.Pets(PetId),
                              CONSTRAINT FK_Bookings_Cages FOREIGN KEY (CageId) REFERENCES dbo.Cages(CageId)
);

-- 7. Bảng BOOKING_SERVICE_DETAILS (Chi tiết sử dụng dịch vụ)
CREATE TABLE dbo.BookingServiceDetails (
                                           DetailId INT IDENTITY(1,1) PRIMARY KEY,
                                           BookingId INT,
                                           ServiceId INT,
                                           Quantity INT,
                                           PriceAtBooking DECIMAL(18,2), -- Lưu giá tại thời điểm đặt

                                           CONSTRAINT FK_Details_Bookings FOREIGN KEY (BookingId) REFERENCES dbo.Bookings(BookingId),
                                           CONSTRAINT FK_Details_Services FOREIGN KEY (ServiceId) REFERENCES dbo.Services(ServiceId)
);


-- ======================================================================================
-- PHẦN 3: SEED DATA (DỮ LIỆU MẪU ĐỂ TEST)
-- ======================================================================================

-- 1. Thêm Users
INSERT INTO dbo.Users (Username, Password, FullName, Role) VALUES
                                                               ('admin', '123', N'Quản Trị Viên', 'Admin'),
                                                               ('staff', '123', N'Nhân Viên 1', 'Staff');

-- 2. Thêm Services
INSERT INTO dbo.Services (ServiceName, Price, Unit) VALUES
                                                        (N'Tắm rửa trọn gói', 150000, N'Lần'),
                                                        (N'Cắt tỉa lông', 200000, N'Lần'),
                                                        (N'Khám sức khỏe', 300000, N'Lần'),
                                                        (N'Spa Massage', 500000, N'Giờ');

-- 3. Thêm Cages
INSERT INTO dbo.Cages (CageName, Type, PricePerDay, Status) VALUES
                                                                (N'VIP 01', N'VIP', 500000, N'Đang ở'),
                                                                (N'VIP 02', N'VIP', 500000, N'Trống'),
                                                                (N'Thường A1', N'Thường', 200000, N'Trống'),
                                                                (N'Thường A2', N'Thường', 200000, N'Bảo trì'),
                                                                (N'Thường A3', N'Thường', 200000, N'Trống');

-- 4. Thêm Customers (Tự sinh KH001, KH002)
INSERT INTO dbo.Customers (FullName, PhoneNumber, Email, Address, Note) VALUES
                                                                            (N'Nguyễn Văn An', '0901234567', 'an@gmail.com', N'Hà Nội', N'Khách VIP'),
                                                                            (N'Trần Thị Bích', '0912345678', 'bich@gmail.com', N'Đà Nẵng', N'Khách mới'),
                                                                            (N'Lê Hoàng Cường', '0988888888', 'cuong@yahoo.com', N'TP HCM', N'Khách khó tính');

-- 5. Thêm Pets (Tự sinh TC001, TC002...)
INSERT INTO dbo.Pets (PetName, Species, Breed, Weight, HealthStatus, CustomerId) VALUES
                                                                                     (N'Misa', N'Mèo', N'Anh lông ngắn', 4.5, N'Bình thường', 1), -- Của ông An
                                                                                     (N'LuLu', N'Chó', N'Poodle', 6.0, N'Bình thường', 2),        -- Của bà Bích
                                                                                     (N'Kiki', N'Chó', N'Corgi', 10.5, N'Đang ốm', 1),            -- Của ông An
                                                                                     (N'Miu Miu', N'Mèo', N'Mèo mướp', 3.2, N'Cần theo dõi', 3);  -- Của ông Cường

-- 6. Thêm Bookings (Đơn đặt phòng mẫu)
-- Đơn 1: Misa (TC001) đang ở chuồng VIP 01
INSERT INTO dbo.Bookings (CustomerId, PetId, CageId, CheckInDate, CheckOutDate, Status, PaymentStatus, TotalPrice) VALUES
    (1, 1, 1, '2026-01-01 08:00:00', '2026-01-05 17:00:00', N'Checked-in', N'Unpaid', 2000000);

-- Đơn 2: LuLu (TC002) đặt trước chuồng Thường A1 nhưng chưa đến
INSERT INTO dbo.Bookings (CustomerId, PetId