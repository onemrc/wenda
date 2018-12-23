package com.demo.wenda.service;

import com.demo.wenda.dao.ProofDAO;
import com.demo.wenda.domain.Proof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by: one
 * create time:2018/12/24 0:19
 * 描述：
 */
@Service
public class ProofService {

    @Autowired
    ProofDAO proofDAO;

    /**
     * 添加身份信息
     *
     * @param proof
     * @return
     */
    public Integer addProof(Proof proof) {
        return proofDAO.addProof(proof);
    }

    /**
     * 获取身份信息By UserId
     *
     * @param userId
     * @return
     */
    public Proof getProofBiUserId(int userId) {
        return proofDAO.getProofByUserId(userId);
    }

}
