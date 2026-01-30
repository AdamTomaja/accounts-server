ALTER TABLE accounts ADD COLUMN reset_password_token VARCHAR(255);
ALTER TABLE accounts ADD COLUMN reset_password_expires_at TIMESTAMP WITH TIME ZONE;;