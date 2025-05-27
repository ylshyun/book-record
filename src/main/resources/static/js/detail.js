const isbn = document.getElementById("bookIsbn").textContent;
const pagination = document.getElementById("pagination");

// 리뷰 저장
function submitReview(event) {
    event.preventDefault(); // 폼 제출 기본 동작 방지
    const reviewContent = document.getElementById("reviewContent").value;
    const data = {
        reviewTitle: document.getElementById("reviewTitle").value,
        reviewContent: reviewContent
    };

    if (reviewContent.length < 50) {
        alert("리뷰는 최소 50자 이상 작성해야 합니다.");
        return; // 서버로 요청하지 않고 종료
    }

    axios.post(`/api/books/${isbn}/reviews`, data)
        .then(response => {
            document.getElementById("reviewForm").style.display = "none"; // 폼 숨기기
            document.getElementById("reviewTitle").value = '';
            document.getElementById("reviewContent").value = '';

            loadReviewList(); // 리뷰 목록 갱신
        })
        .catch(error => {
            console.error("리뷰 작성 중 오류가 발생했습니다.");
        });
}

// 리뷰 목록 불러오기
function loadReviewList(page=0, size=10) {
    const loggedInUserEmail = document.getElementById("LoginMemberEmail").textContent;
    axios.get(`/api/books/${isbn}/reviews?page=${page}&size=${size}`)
        .then(response => {
            const reviewList = document.getElementById("reviewList");
            reviewList.innerHTML = ""; // 기존 리뷰 목록 초기화

            response.data.content.forEach(review => {
                const div = document.createElement("div");
                div.classList.add("review-card");
                div.id = `review-${review.reviewId}`;

                let buttons = '';
                if (review.memberEmail === loggedInUserEmail) {
                    buttons = `
                        <div class="review-button">
                            <button class="btn btn-outline-dark" id="editButton-${review.reviewId}" onclick="showEditForm(${review.reviewId})">수정</button>
                            <button class="btn btn-outline-dark" id="deleteButton-${review.reviewId}" onclick="deleteReview(${review.reviewId})">삭제</button>
                        </div>
                    `;
                }

                div.innerHTML = `
                    <div class="review-title">
                        <span class="title-text">${review.reviewTitle}</span>
                        ${buttons}
                    </div>
                    <span class="review-writer">${review.memberName} | ${review.modifiedDate}</span><br>
                    <div class="review-content">${review.reviewContent}</div>
                `;
                reviewList.appendChild(div);
            });

            // 페이징
            pagination.innerText='';

            for (let i = 1; i < response.data.totalPages; i++) {
                const pageButton = document.createElement("button");
                pageButton.textContent = i;
                pageButton.onclick = () => loadReviewList(i, size);
                pagination.appendChild(pageButton);
            }
        })
        .catch(error => {
            console.error('리뷰를 불러오는데 실패했습니다.', error);
        });
}

document.addEventListener('DOMContentLoaded', async () => {
    loadReviewList();
});


// 리뷰 수정 폼
function showEditForm(reviewId) {
    const reviewCard = document.getElementById(`review-${reviewId}`);

    const title = reviewCard.querySelector(".title-text").textContent.trim();
    const content = reviewCard.querySelector(".review-content").textContent.trim();

    // 수정 폼에 기존 리뷰 내용 채워넣기
    document.getElementById("edit-reviewTitle").value = title;
    document.getElementById("edit-reviewContent").value = content;

    const editForm = document.getElementById("editReviewForm");
    editForm.dataset.reviewId = reviewId;

    // 기존 목록을 숨기고 수정 폼으로 교체
    editForm.style.display = "block";
    reviewCard.style.display = "none";
    // 페이징 숨기기
    pagination.style.display = "none";
}

// 리뷰 수정 취소
function cancelEdit(reviewId) {
    // 수정 폼 숨기기
    document.getElementById("editReviewForm").style.display = "none";

    // 기존 리뷰 목록, 페이징 다시 보이게 하기
    loadReviewList();
    pagination.style.display = "block";
}

// 리뷰 수정
function submitEdit(event) {
    event.preventDefault();
    const reviewId = document.getElementById("editReviewForm").dataset.reviewId;
    const reviewContent = document.getElementById("edit-reviewContent").value
    const data = {
        reviewTitle: document.getElementById("edit-reviewTitle").value,
        reviewContent: reviewContent
    };

    if (reviewContent.length < 50) {
        alert("리뷰는 최소 50자 이상 작성해야 합니다.");
        return;
    }

    axios.put(`/api/books/${isbn}/reviews/${reviewId}`, data)
        .then(response => {
            alert("리뷰가 수정 되었습니다.");
            loadReviewList();
            document.getElementById("editReviewForm").style.display="none";
        })
        .catch(error => {
            console.error("리뷰 수정 중 오류가 발생했습니다.");
        });
}

// 리뷰 삭제
function deleteReview(reviewId){
    if (!confirm("해당 리뷰를 삭제하시겠습니까?")) return;

    //삭제 버튼 비활성화
    const deleteButton = document.getElementById(`deleteButton-${reviewId}`)
    if(deleteButton){
        deleteButton.disabled = true;
    }

    axios.delete(`/api/books/${isbn}/reviews/${reviewId}`)
        .then(response => {
        alert("리뷰가 삭제되었습니다.");
        loadReviewList();
        document.getElementById("reviewForm").style.display = "block" // 리뷰 작성 폼 다시 생성
    })
    .catch(error => {
        console.error("리뷰 삭제 중 오류가 발생했습니다.", error);
        alert("리뷰 삭제 중 오류가 발생했습니다. 다시 시도해주세요.");
    })
        .finally(()=> {
            if (deleteButton){
                deleteButton.disabled = false; // 삭제 버튼 활성화
            }
        })
}
