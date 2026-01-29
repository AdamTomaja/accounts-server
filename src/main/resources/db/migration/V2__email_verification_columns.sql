ALTER TABLE accounts ADD COLUMN email_verified_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE accounts ADD COLUMN email_verification_token VARCHAR(255);