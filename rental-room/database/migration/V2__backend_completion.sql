-- ============================================
-- RENTAL ROOM BACKEND - MIGRATION V2
-- Run this script manually because ddl-auto=none
-- ============================================

-- 1. Room approval workflow
ALTER TABLE rooms
    ADD COLUMN approval_status VARCHAR(20) NOT NULL DEFAULT 'APPROVED';

UPDATE rooms
SET approval_status = 'APPROVED'
WHERE approval_status IS NULL OR approval_status = '';

-- 2. Viewing appointment workflow
CREATE TABLE IF NOT EXISTS viewing_appointments (
    id INT NOT NULL AUTO_INCREMENT,
    customer_id INT NOT NULL,
    landlord_id INT NOT NULL,
    room_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    message VARCHAR(1000) NULL,
    status VARCHAR(20) NOT NULL,
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_viewing_customer
        FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT fk_viewing_landlord
        FOREIGN KEY (landlord_id) REFERENCES users(id),
    CONSTRAINT fk_viewing_room
        FOREIGN KEY (room_id) REFERENCES rooms(id)
);
