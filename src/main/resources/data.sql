INSERT INTO members (id, full_name, active) VALUES
                                                ('11111111-1111-1111-1111-111111111111', 'Иванов Иван Иванович', true),
                                                ('22222222-2222-2222-2222-222222222222', 'Петров Пётр Петрович', true),
                                                ('33333333-3333-3333-3333-333333333333', 'Сидоров Сидор Сидорович', false);

INSERT INTO qr_codes (id, member_id, is_active, created_at) VALUES
                                                                ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', true, NOW()),
                                                                ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', true, NOW()),
                                                                ('cccccccc-cccc-cccc-cccc-cccccccccccc', '11111111-1111-1111-1111-111111111111', false, NOW() - INTERVAL '1 day');
