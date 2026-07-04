-- ============================================================
-- 种子数据脚本
-- 密码均为明文，仅用于开发/测试环境
-- ============================================================

-- 用户表
-- 管理员
INSERT INTO user (id, userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole)
VALUES (1, 'admin', 'admin', NULL, NULL, '管理员', NULL, '系统管理员', 'admin');

-- 普通用户（应用创建者）
INSERT INTO user (id, userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole)
VALUES (2, 'zhangsan', '1234', NULL, NULL, '张三', NULL, '热爱心理学和性格测试', 'user');

-- 普通用户
INSERT INTO user (id, userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole)
VALUES (3, 'lisi', '1234', NULL, NULL, '李四', NULL, '喜欢探索自我', 'user');

-- ============================================================
-- 应用表（MBTI 性格测试，由张三创建，待审核）
-- ============================================================
INSERT INTO app (id, appName, appDesc, appIcon, appType, scoringStrategy, reviewStatus, reviewMessage, reviewerId, reviewTime, userId)
VALUES (1,
        'MBTI 性格测试',
        '基于荣格心理类型理论的经典性格测评。MBTI（Myers-Briggs Type Indicator）将人格分为 4 个维度：'
            '精力来源（外向 E / 内向 I）、认知方式（感觉 S / 直觉 N）、'
            '决策依据（思维 T / 情感 F）、生活方式（判断 J / 感知 P），'
            '组合出 16 种人格类型。通过 10 道精选题目，帮助你快速了解自己的性格倾向。',
        NULL,
        1, -- 测评类
        0, -- 自定义评分策略
        0, -- 待审核
        NULL,
        NULL,
        NULL,
        2); -- 创建用户：张三

-- ============================================================
-- 题目表（10 道 MBTI 题目，由张三创建）
-- ============================================================
INSERT INTO question (id, questionContent, appId, userId) VALUES
(1, '{"title":"你通常更喜欢","options":[{"key":"A","value":"独自工作","result":"I"},{"key":"B","value":"与他人合作","result":"E"}]}', 1, 2),
(2, '{"title":"当安排活动时","options":[{"key":"A","value":"喜欢有明确的计划","result":"J"},{"key":"B","value":"更愿意随机应变","result":"P"}]}', 1, 2),
(3, '{"title":"你如何看待规则","options":[{"key":"A","value":"认为应该严格遵守","result":"T"},{"key":"B","value":"认为应灵活运用","result":"F"}]}', 1, 2),
(4, '{"title":"在社交场合中","options":[{"key":"A","value":"经常是说话的人","result":"E"},{"key":"B","value":"更倾向于倾听","result":"I"}]}', 1, 2),
(5, '{"title":"面对新的挑战","options":[{"key":"A","value":"先研究再行动","result":"J"},{"key":"B","value":"边做边学习","result":"P"}]}', 1, 2),
(6, '{"title":"在日常生活中","options":[{"key":"A","value":"注重细节和事实","result":"S"},{"key":"B","value":"注重概念和想象","result":"N"}]}', 1, 2),
(7, '{"title":"做决定时","options":[{"key":"A","value":"更多基于逻辑分析","result":"T"},{"key":"B","value":"更多基于个人情感","result":"F"}]}', 1, 2),
(8, '{"title":"对于日常安排","options":[{"key":"A","value":"喜欢有结构和常规","result":"S"},{"key":"B","value":"喜欢自由和灵活性","result":"N"}]}', 1, 2),
(9, '{"title":"当遇到问题时","options":[{"key":"A","value":"首先考虑可能性","result":"P"},{"key":"B","value":"首先考虑后果","result":"J"}]}', 1, 2),
(10,'{"title":"你如何看待时间","options":[{"key":"A","value":"时间是一种宝贵的资源","result":"T"},{"key":"B","value":"时间是相对灵活的概念","result":"F"}]}', 1, 2);

-- ============================================================
-- 评分结果表（16 种 MBTI 人格类型，由张三创建）
-- ============================================================
INSERT INTO scoring_result (id, resultName, resultDesc, resultPicture, resultProp, resultScoreRange, appId, userId) VALUES
(1,  'ISTJ（物流师）', '忠诚可靠，被公认为务实，注重细节。', NULL, '["I","S","T","J"]', NULL, 1, 2),
(2,  'ISFJ（守护者）', '善良贴心，以同情心和责任为特点。', NULL, '["I","S","F","J"]', NULL, 1, 2),
(3,  'INFJ（占有者）', '理想主义者，有着深刻的洞察力，善于理解他人。', NULL, '["I","N","F","J"]', NULL, 1, 2),
(4,  'INTJ（设计师）', '独立思考者，善于规划和实现目标，理性而果断。', NULL, '["I","N","T","J"]', NULL, 1, 2),
(5,  'ISTP（运动员）', '冷静自持，善于解决问题，擅长实践技能。', NULL, '["I","S","T","P"]', NULL, 1, 2),
(6,  'ISFP（艺术家）', '具有艺术感和敏感性，珍视个人空间和自由。', NULL, '["I","S","F","P"]', NULL, 1, 2),
(7,  'INFP（治愈者）', '理想主义者，富有创造力，以同情心和理解他人著称。', NULL, '["I","N","F","P"]', NULL, 1, 2),
(8,  'INTP（学者）',   '思维清晰，探索精神，独立思考且理性。', NULL, '["I","N","T","P"]', NULL, 1, 2),
(9,  'ESTP（拓荒者）', '敢于冒险，乐于冒险，思维敏捷，行动果断。', NULL, '["E","S","T","P"]', NULL, 1, 2),
(10, 'ESFP（表演者）', '热情开朗，善于社交，热爱生活，乐于助人。', NULL, '["E","S","F","P"]', NULL, 1, 2),
(11, 'ENFP（倡导者）', '富有想象力，充满热情，善于激发他人的活力和潜力。', NULL, '["E","N","F","P"]', NULL, 1, 2),
(12, 'ENTP（发明家）', '充满创造力，善于辩论，挑战传统，喜欢探索新领域。', NULL, '["E","N","T","P"]', NULL, 1, 2),
(13, 'ESTJ（主管）',   '务实果断，善于组织和管理，重视效率和目标。', NULL, '["E","S","T","J"]', NULL, 1, 2),
(14, 'ESFJ（尽责者）', '友善热心，以协调、耐心和关怀为特点，善于团队合作。', NULL, '["E","S","F","J"]', NULL, 1, 2),
(15, 'ENFJ（教导着）', '热情关爱，善于帮助他人，具有领导力和社交能力。', NULL, '["E","N","F","J"]', NULL, 1, 2),
(16, 'ENTJ（统帅）',   '果断自信，具有领导才能，善于规划和执行目标。', NULL, '["E","N","T","J"]', NULL, 1, 2);
