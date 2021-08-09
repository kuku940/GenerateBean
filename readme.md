mybatis generate bean

    注意：
        @TableId(value="id", tyoe=IdType.AUTO)
        private Integer id;

    否则，数据库新增数据id不会自增长，会随机很大