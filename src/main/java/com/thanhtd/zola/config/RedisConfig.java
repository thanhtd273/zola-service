package com.thanhtd.zola.config;

import io.lettuce.core.ClientOptions;
import lombok.NoArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Configuration
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@AutoConfigureBefore({RedisConfiguration.class})
@EnableConfigurationProperties({RedisProperties.class})
@NoArgsConstructor
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    /** The Constant Redis prefix. */
    private static final String REDIS_PREFIX = "redis://";

    /** The Constant Redis SSL prefix. */
    private static final String REDIS_SSL_PREFIX = "rediss://";
    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.redis.port}")
    private int REDIS_PORT;

    @Value("${spring.redis.password}")
    private String REDIS_PASSWORD;

    @Value("${redis.connection.timeout}")
    private int REDIS_CONNECTION_TIMEOUT;

    @Value("${redis.read.timeout}")
    private int REDIS_READ_TIMEOUT;

    @Value("${redis.max.wait.millis}")
    private int MAX_WAIT_MILLIS;

    @Value("${redis.max.total}")
    private int REDIS_MAX_TOTAL;

    @Value("${redis.min.idle}")
    private int REDIS_MIN_IDLE;

    @Value("${redis.max.idle}")
    private int REDIS_MAX_IDLE;

    @Value("${spring.redis.client-type}")
    private String CLIENT_TYPE;

    @Value("${spring.redis.cluster.max-redirects}")
    private int CLUSTER_MAX_DIRECTS;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (redisProperties.getCluster() == null || redisProperties.getCluster().getNodes().isEmpty()) {
            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
            redisStandaloneConfiguration.setPassword(RedisPassword.of(REDIS_PASSWORD));
            redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
            JedisClientConfiguration jedisClientConfig = getJedisClientConfig();
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfig);
            jedisConnectionFactory.afterPropertiesSet();
            return jedisConnectionFactory;
        }
        redisProperties.getCluster().getNodes().forEach(node -> logger.info("Cluster node: {}", node));
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
        redisClusterConfiguration.setMaxRedirects(CLUSTER_MAX_DIRECTS);
        redisClusterConfiguration.setPassword(RedisPassword.of(REDIS_PASSWORD));

        if (!"lettuce".equals(CLIENT_TYPE)) {
            JedisClientConfiguration clientConfig = getJedisClientConfig();
            JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration, clientConfig);
            factory.afterPropertiesSet();;
            return factory;
        }

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration
                .builder()
                .shutdownTimeout(Duration.ofSeconds(REDIS_CONNECTION_TIMEOUT))
                .commandTimeout(Duration.ofSeconds(REDIS_READ_TIMEOUT))
                .clientOptions(ClientOptions.builder().build())
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
        factory.afterPropertiesSet();;
        return factory;
    }

    private JedisClientConfiguration getJedisClientConfig(){
        return JedisClientConfiguration
                .builder()
                .connectTimeout(Duration.ofSeconds(REDIS_CONNECTION_TIMEOUT))
                .readTimeout(Duration.ofSeconds(REDIS_READ_TIMEOUT))
                .usePooling()
                .poolConfig(jedisPoolConfig())
                .build();
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWait(Duration.ofSeconds(MAX_WAIT_MILLIS));
        jedisPoolConfig.setMaxTotal(REDIS_MAX_TOTAL);
        jedisPoolConfig.setMinIdle(REDIS_MIN_IDLE);
        jedisPoolConfig.setMaxIdle(REDIS_MAX_IDLE);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnCreate(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinEvictableIdleTime(Duration.ofSeconds(1800000));
        jedisPoolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setBlockWhenExhausted(true);

        return jedisPoolConfig;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean({RedisConnectionFactory.class})
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

//    @Bean(destroyMethod = "shutdown")
//    @ConditionalOnMissingBean({RedissonClient.class})
//    public RedissonClient redisson() {
//        Config config = new Config();
////        if (!redisProperties.getCluster().getNodes().isEmpty()) {
////            String[] listNodes = convert(redisProperties.getCluster().getNodes());
////            config.useClusterServers()
////                    .addNodeAddress(listNodes)
////                    .setConnectTimeout(REDIS_CONNECTION_TIMEOUT)
////                    .setPassword(redisProperties.getPassword());
////        } else {
//            String prefix = REDIS_PREFIX;
//            config.useSingleServer()
//                    .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
//                    .setConnectTimeout(REDIS_CONNECTION_TIMEOUT)
//                    .setDatabase(redisProperties.getDatabase())
//                    .setPassword(redisProperties.getPassword());
////        }
//        return Redisson.create(config);
//    }

    private String[] convert(List<String> nodesObject) {
        List<String> listNode = new ArrayList<>(nodesObject.size());
        Iterator var3 = nodesObject.iterator();
        while (true) {
            while (var3.hasNext()) {
                String node = (String) var3.next();
                if (!node.startsWith(REDIS_PREFIX) && !node.startsWith(REDIS_SSL_PREFIX)) {
                    listNode.add(REDIS_PREFIX + node);
                } else {
                    listNode.add(node);
                }
            }
            return listNode.toArray(new String[listNode.size()]);
        }
    }
}
