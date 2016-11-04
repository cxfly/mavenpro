package com.cxfly.test.hardware;

import java.util.List;

public class Test {
	public static void main(String[] args) throws Exception {
		List<SigarInfoEntity> cpuInfos = SigarUtils.getCpuInfos();
		for (SigarInfoEntity sigarInfoEntity : cpuInfos) {
			System.out.println(sigarInfoEntity);
		}
	}
}
